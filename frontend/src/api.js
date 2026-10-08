// Cliente para el login service y la API de productos (via proxy de Vite).
const form = (obj) => new URLSearchParams(obj).toString()
const HEADERS = { 'Content-Type': 'application/x-www-form-urlencoded' }

async function leerTexto(res) {
  const texto = await res.text()
  try {
    const json = JSON.parse(texto)
    return json.mensaje || json.message || json.error || texto
  } catch {
    return texto
  }
}

export async function login(usuario, contrasena) {
  const res = await fetch('/auth/LoginServlet', {
    method: 'POST',
    headers: HEADERS,
    body: form({ usuario, contrasena }),
  })
  return { ok: res.ok, mensaje: await leerTexto(res) }
}

export async function registrar(usuario, contrasena, correo) {
  const res = await fetch('/auth/RegistroServlet', {
    method: 'POST',
    headers: HEADERS,
    body: form({ usuario, contrasena, correo }),
  })
  return { ok: res.ok, mensaje: await leerTexto(res) }
}

export async function listarProductos() {
  const res = await fetch('/stock/api/productos')
  if (!res.ok) throw new Error('No se pudo cargar la lista de productos')
  const data = await res.json()
  return Array.isArray(data) ? data : data.productos || []
}

export async function crearProducto(p) {
  const res = await fetch('/stock/api/productos', {
    method: 'POST',
    headers: HEADERS,
    body: form(p),
  })
  if (!res.ok) throw new Error(await leerTexto(res))
}

export async function editarProducto(id, p) {
  const res = await fetch(`/stock/api/productos/${id}`, {
    method: 'PUT',
    headers: HEADERS,
    body: form(p),
  })
  if (!res.ok) throw new Error(await leerTexto(res))
}

export async function eliminarProducto(id) {
  const res = await fetch(`/stock/api/productos/${id}`, { method: 'DELETE' })
  if (!res.ok) throw new Error(await leerTexto(res))
}
