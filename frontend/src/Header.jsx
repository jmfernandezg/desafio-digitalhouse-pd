import React from 'react';
import './Header.css';
import DBDLogo from './components/DBDLogo'
import {LogIn, User} from 'lucide-react';
import {Button} from "@mui/material";

function Header() {
    return (
        <header>
            <a href="/" className="logo"></a>
            <a href="/" className="tagline">Sentite como en tu hogar</a>
            <div className="auth-buttons">
                <Button variant="soft" radius="full">
                    <User size={16}></User> Crear cuenta
                </Button>
                <Button variant="solid">
                    <LogIn size={16}/> Iniciar sesión
                </Button>
            </div>
        </header>
    );
}

export default Header;