import React from 'react';
import {LogIn, User} from 'lucide-react';
import {Link} from 'react-router-dom';
import './Header.css';

const Button = ({children, variant = 'soft', onClick}) => {
    const baseStyles = "flex items-center gap-2 px-4 py-2 rounded-full text-sm font-medium transition-colors duration-200";

    const variants = {
        soft: "bg-blue-50 text-blue-600 hover:bg-blue-100",
        solid: "bg-blue-600 text-white hover:bg-blue-700"
    };

    return (
        <button
            onClick={onClick}
            className={`${baseStyles} ${variants[variant]}`}>
            {children}
        </button>
    );
};

function Header() {
    return (
        <header className="header border-b border-gray-100">
            <div className="max-w-7xl mx-auto px-4 h-20 flex items-center justify-between">
                <div className="flex items-center gap-8">
                    <Link
                        to="/"
                        className="text-2xl font-bold text-blue-600 logo"
                        aria-label="Home"
                    >
                    </Link>
                    <Link
                        to="/"
                        className="text-gray-600 hover:text-gray-900 font-medium text-sm"
                    >
                        Sentite como en tu hogar
                    </Link>
                </div>

                <div className="flex items-center gap-4">
                    <Button variant="soft">
                        <User size={16}/>
                        Crear cuenta
                    </Button>
                    <Button variant="solid">
                        <LogIn size={16}/>
                        Iniciar sesión
                    </Button>
                </div>
            </div>
        </header>
    );
}

export default Header;