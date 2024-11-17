import React from 'react';
import './Header.css';
import { LogIn, User } from 'lucide-react';

function Header() {
    return (
        <header>
            <a href="/" className="logo"></a>
            <a href="/" className="tagline">Sentite como en tu hogar</a>
            <div className="auth-buttons">
                <button>
                    <User size={16}/> Crear cuenta
                </button>
                <button>
                    <LogIn size={16}/> Iniciar sesión
                </button>
            </div>
        </header>
    );
}

export default Header;