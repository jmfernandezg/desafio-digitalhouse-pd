import React from 'react';
import {Home, LogIn, User} from 'lucide-react';
import {Link} from 'react-router-dom';

const Button = ({children, variant = 'soft', onClick}) => {
    const baseStyles = "flex items-center gap-2 px-4 py-2 rounded-lg text-sm font-medium transition-all duration-300 shadow-sm hover:shadow-md";

    const variants = {
        soft: "bg-white/80 text-blue-600 hover:bg-white hover:scale-105",
        solid: "bg-blue-600 text-white hover:bg-blue-700 hover:scale-105"
    };

    return (
        <button
            onClick={onClick}
            className={`${baseStyles} ${variants[variant]}`}
        >
            {children}
        </button>
    );
};

const Tagline = () => (
    <div className="group relative flex items-center gap-2 px-4 py-2 cursor-default">
        <Home className="h-4 w-4 text-blue-600 group-hover:animate-bounce"/>
        <span className="font-serif italic relative">
      Sentite como en tu hogar
      <span className="absolute -bottom-1 left-0 w-0 h-0.5 bg-blue-600 transition-all duration-300 group-hover:w-full"/>
    </span>
        <div
            className="absolute inset-0 bg-blue-50/50 rounded-lg scale-0 group-hover:scale-100 transition-transform duration-300 -z-10"/>
    </div>
);

const Header = () => {
    return (
        <header className="bg-gradient-to-r from-sky-100 to-blue-100 border-b border-blue-100 shadow-sm">
            <div className="max-w-7xl mx-auto px-6 h-20 flex items-center justify-between">
                <div className="flex items-center gap-8">
                    <Link
                        to="/"
                        className="flex items-center transition-transform duration-300 hover:scale-105"
                        aria-label="Digital Booking Home"
                    >
                        <img
                            src="/logo.png"
                            alt="Digital Booking"
                            className="h-14 object-container brightness-110 contrast-125"
                        />
                    </Link>

                    <Tagline/>
                </div>

                <div className="flex items-center gap-4">
                    <Button variant="soft">
                        <User className="h-4 w-4"/>
                        <span>Crear cuenta</span>
                    </Button>
                    <Button variant="solid">
                        <LogIn className="h-4 w-4"/>
                        <span>Iniciar sesión</span>
                    </Button>
                </div>
            </div>
        </header>
    );
};

export default Header;