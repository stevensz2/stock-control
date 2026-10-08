export default function Navbar({ usuario, onSalir }) {
  return (
    <header className="navbar">
      <span className="marca">Stock Control</span>
      {usuario && (
        <div className="sesion">
          <span>Sesión de {usuario}</span>
          <button className="btn btn-claro" onClick={onSalir}>
            Cerrar sesión
          </button>
        </div>
      )}
    </header>
  )
}
