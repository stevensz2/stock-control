import { useEffect, useState } from 'react'
import { listarProductos, crearProducto, editarProducto, eliminarProducto } from './api.js'

const VACIO = { nombre: '', descripcion: '', precio: '', cantidad: '' }
const idDe = (p) => p.id ?? p.idProducto ?? p.id_producto
const moneda = (n) => Number(n).toLocaleString('es-CO', { style: 'currency', currency: 'COP', maximumFractionDigits: 0 })

export default function ProductosView() {
  const [productos, setProductos] = useState([])
  const [form, setForm] = useState(VACIO)
  const [editandoId, setEditandoId] = useState(null)
  const [aviso, setAviso] = useState(null)

  async function cargar() {
    try {
      setProductos(await listarProductos())
    } catch (e) {
      setAviso({ tipo: 'error', texto: e.message })
    }
  }

  useEffect(() => {
    cargar()
  }, [])

  const cambiar = (campo) => (e) => setForm({ ...form, [campo]: e.target.value })

  async function guardar(e) {
    e.preventDefault()
    setAviso(null)
    try {
      if (editandoId === null) {
        await crearProducto(form)
        setAviso({ tipo: 'ok', texto: 'Producto creado' })
      } else {
        await editarProducto(editandoId, form)
        setAviso({ tipo: 'ok', texto: 'Producto actualizado' })
      }
      setForm(VACIO)
      setEditandoId(null)
      cargar()
    } catch (err) {
      setAviso({ tipo: 'error', texto: err.message || 'No se pudo guardar' })
    }
  }

  function empezarEdicion(p) {
    setEditandoId(idDe(p))
    setForm({
      nombre: p.nombre ?? '',
      descripcion: p.descripcion ?? '',
      precio: p.precio ?? '',
      cantidad: p.cantidad ?? '',
    })
    window.scrollTo({ top: 0, behavior: 'smooth' })
  }

  function cancelar() {
    setEditandoId(null)
    setForm(VACIO)
  }

  async function borrar(p) {
    if (!window.confirm(`¿Eliminar "${p.nombre}"?`)) return
    try {
      await eliminarProducto(idDe(p))
      setAviso({ tipo: 'ok', texto: 'Producto eliminado' })
      cargar()
    } catch (err) {
      setAviso({ tipo: 'error', texto: err.message || 'No se pudo eliminar' })
    }
  }

  return (
    <>
      <section className="tarjeta">
        <h2>{editandoId === null ? 'Nuevo producto' : `Editar producto ${editandoId}`}</h2>
        <form className="rejilla" onSubmit={guardar}>
          <label>
            Nombre
            <input value={form.nombre} onChange={cambiar('nombre')} required />
          </label>
          <label>
            Descripción
            <input value={form.descripcion} onChange={cambiar('descripcion')} />
          </label>
          <label>
            Precio
            <input type="number" min="0" step="any" value={form.precio} onChange={cambiar('precio')} required />
          </label>
          <label>
            Cantidad
            <input type="number" min="0" step="1" value={form.cantidad} onChange={cambiar('cantidad')} required />
          </label>
          <div className="acciones">
            <button className="btn">{editandoId === null ? 'Guardar producto' : 'Guardar cambios'}</button>
            {editandoId !== null && (
              <button type="button" className="btn btn-claro" onClick={cancelar}>
                Cancelar
              </button>
            )}
          </div>
        </form>
        {aviso && <p className={`aviso ${aviso.tipo}`}>{aviso.texto}</p>}
      </section>

      <section className="tarjeta">
        <h2>Productos</h2>
        {productos.length === 0 ? (
          <p className="vacio">Aún no hay productos. Agrega el primero con el formulario.</p>
        ) : (
          <div className="tabla-scroll">
            <table>
              <thead>
                <tr>
                  <th>ID</th>
                  <th>Nombre</th>
                  <th>Descripción</th>
                  <th className="num">Precio</th>
                  <th className="num">Cantidad</th>
                  <th></th>
                </tr>
              </thead>
              <tbody>
                {productos.map((p) => (
                  <tr key={idDe(p)}>
                    <td>{idDe(p)}</td>
                    <td>{p.nombre}</td>
                    <td>{p.descripcion}</td>
                    <td className="num">{moneda(p.precio)}</td>
                    <td className="num">{p.cantidad}</td>
                    <td className="fila-acciones">
                      <button className="btn btn-claro" onClick={() => empezarEdicion(p)}>
                        Editar
                      </button>
                      <button className="btn btn-peligro" onClick={() => borrar(p)}>
                        Eliminar
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </section>
    </>
  )
}
