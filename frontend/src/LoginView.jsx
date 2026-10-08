import { useState } from 'react'
import { login, registrar } from './api.js'

export default function LoginView({ onEntrar }) {
  const [modo, setModo] = useState('login')
  const [usuario, setUsuario] = useState('')
  const [contrasena, setContrasena] = useState('')
  const [correo, setCorreo] = useState('')
  const [aviso, setAviso] = useState(null)
  const [cargando, setCargando] = useState(false)

  async function enviar(e) {
    e.preventDefault()
    setAviso(null)
    setCargando(true)
    try {
      if (modo === 'login') {
        const r = await login(usuario, contrasena)
        if (r.ok) onEntrar(usuario)
        else setAviso({ tipo: 'error', texto: r.mensaje || 'Usuario o contraseña incorrectos' })
      } else {
        const r = await registrar(usuario, contrasena, correo)
        setAviso({ tipo: r.ok ? 'ok' : 'error', texto: r.mensaje })
        if (r.ok) setModo('login')
      }
    } catch {
      setAviso({ tipo: 'error', texto: 'No hay conexión con el servidor. Revisa que Tomcat esté encendido.' })
    } finally {
      setCargando(false)
    }
  }

  return (
    <section className="tarjeta login">
      <h1>{modo === 'login' ? 'Iniciar sesión' : 'Crear cuenta'}</h1>
      <form onSubmit={enviar}>
        <label>
          Usuario
          <input value={usuario} onChange={(e) => setUsuario(e.target.value)} required autoFocus />
        </label>
        <label>
          Contraseña
          <input type="password" value={contrasena} onChange={(e) => setContrasena(e.target.value)} required />
        </label>
        {modo === 'registro' && (
          <label>
            Correo
            <input type="email" value={correo} onChange={(e) => setCorreo(e.target.value)} required />
          </label>
        )}
        {aviso && <p className={`aviso ${aviso.tipo}`}>{aviso.texto}</p>}
        <button className="btn" disabled={cargando}>
          {cargando ? 'Enviando…' : modo === 'login' ? 'Entrar' : 'Registrarme'}
        </button>
      </form>
      <button
        className="enlace"
        onClick={() => {
          setAviso(null)
          setModo(modo === 'login' ? 'registro' : 'login')
        }}
      >
        {modo === 'login' ? 'Crear una cuenta' : 'Ya tengo cuenta'}
      </button>
    </section>
  )
}
