import { useState } from 'react'
import Navbar from './Navbar.jsx'
import LoginView from './LoginView.jsx'
import ProductosView from './ProductosView.jsx'

export default function App() {
  const [usuario, setUsuario] = useState(null)

  return (
    <>
      <Navbar usuario={usuario} onSalir={() => setUsuario(null)} />
      <main className="contenedor">
        {usuario ? <ProductosView /> : <LoginView onEntrar={setUsuario} />}
      </main>
    </>
  )
}
