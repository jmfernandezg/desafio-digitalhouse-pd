import React from 'react';
import './Header.css';

function Header() {
    return (
        <header>
            <div className="logo"></div>
            <div className="tagline">Sentite como en tu hogar</div>
            <div className="auth-buttons">
                <button>Crear cuenta</button>
                <button>Iniciar sesión</button>
            </div>
        </header>
    );
}

export default Header;