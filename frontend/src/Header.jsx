import React, { useState } from 'react';
import { Home, LogIn, User, X, AlertCircle } from 'lucide-react';
import { Link } from 'react-router-dom';
import CustomerService from './services/CustomerService';
import { Alert, AlertDescription } from '@/components/ui/alert';
import {
    Dialog,
    DialogContent,
    DialogHeader,
    DialogTitle,
    DialogFooter
} from "@/components/ui/dialog";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";

const NavButton = ({ children, variant = 'soft', onClick }) => {
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
        <Home className="h-4 w-4 text-blue-600 group-hover:animate-bounce" />
        <span className="font-serif italic relative">
            Sentite como en tu hogar
            <span className="absolute -bottom-1 left-0 w-0 h-0.5 bg-blue-600 transition-all duration-300 group-hover:w-full" />
        </span>
        <div className="absolute inset-0 bg-blue-50/50 rounded-lg scale-0 group-hover:scale-100 transition-transform duration-300 -z-10" />
    </div>
);

const LoginDialog = ({ isOpen, onClose, onSuccess }) => {
    const [loginData, setLoginData] = useState({ email: '', password: '' });
    const [error, setError] = useState('');
    const [isLoading, setIsLoading] = useState(false);

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');
        setIsLoading(true);

        try {
            const response = await CustomerService.login(loginData);

            if (response.authToken) {
                // Store token
                localStorage.setItem('authToken', response.authToken);
                onSuccess(response);
                onClose();
            } else {
                setError('Error en la respuesta del servidor');
            }
        } catch (err) {
            setError(err.response?.data?.error || 'Error al iniciar sesión');
        } finally {
            setIsLoading(false);
        }
    };

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setLoginData(prev => ({ ...prev, [name]: value }));
        setError(''); // Clear error when user types
    };

    return (
        <Dialog open={isOpen} onOpenChange={onClose}>
            <DialogContent className="sm:max-w-[425px]">
                <DialogHeader>
                    <DialogTitle>Iniciar sesión</DialogTitle>
                </DialogHeader>

                <form onSubmit={handleSubmit} className="space-y-4 mt-4">
                    {error && (
                        <Alert variant="destructive" className="mb-4">
                            <AlertCircle className="h-4 w-4" />
                            <AlertDescription>{error}</AlertDescription>
                        </Alert>
                    )}

                    <div className="space-y-2">
                        <label htmlFor="email" className="text-sm font-medium">
                            Correo electrónico
                        </label>
                        <Input
                            id="email"
                            name="email"
                            type="email"
                            value={loginData.email}
                            onChange={handleInputChange}
                            placeholder="tucorreo@ejemplo.com"
                            required
                            className="w-full"
                        />
                    </div>

                    <div className="space-y-2">
                        <label htmlFor="password" className="text-sm font-medium">
                            Contraseña
                        </label>
                        <Input
                            id="password"
                            name="password"
                            type="password"
                            value={loginData.password}
                            onChange={handleInputChange}
                            placeholder="••••••••"
                            required
                            className="w-full"
                        />
                    </div>

                    <DialogFooter className="mt-6">
                        <Button
                            type="button"
                            variant="outline"
                            onClick={onClose}
                            className="mr-2"
                        >
                            Cancelar
                        </Button>
                        <Button
                            type="submit"
                            disabled={isLoading}
                            className="bg-blue-600 hover:bg-blue-700"
                        >
                            {isLoading ? 'Iniciando sesión...' : 'Iniciar sesión'}
                        </Button>
                    </DialogFooter>
                </form>
            </DialogContent>
        </Dialog>
    );
};

const Header = () => {
    const [isLoginOpen, setIsLoginOpen] = useState(false);
    const [isLoggedIn, setIsLoggedIn] = useState(false);
    const [user, setUser] = useState(null);

    const handleLoginSuccess = (loginResponse) => {
        setIsLoggedIn(true);
        setUser(loginResponse);
    };

    const handleLogout = () => {
        localStorage.removeItem('authToken');
        setIsLoggedIn(false);
        setUser(null);
    };

    return (
        <div className="fixed top-0 left-0 right-0 z-50">
            <header className="bg-gradient-to-r from-sky-100 to-blue-100 border-b border-blue-100 shadow-lg backdrop-blur-sm bg-opacity-90">
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

                        <Tagline />
                    </div>

                    <div className="flex items-center gap-4">
                        {isLoggedIn ? (
                            <>
                                <div className="text-sm text-blue-600">
                                    Bienvenido, {user?.firstName || 'Usuario'}
                                </div>
                                <NavButton variant="soft" onClick={handleLogout}>
                                    <LogIn className="h-4 w-4" />
                                    <span>Cerrar sesión</span>
                                </NavButton>
                            </>
                        ) : (
                            <>
                                <NavButton variant="soft">
                                    <User className="h-4 w-4" />
                                    <span>Crear cuenta</span>
                                </NavButton>
                                <NavButton
                                    variant="solid"
                                    onClick={() => setIsLoginOpen(true)}
                                >
                                    <LogIn className="h-4 w-4" />
                                    <span>Iniciar sesión</span>
                                </NavButton>
                            </>
                        )}
                    </div>
                </div>
            </header>

            <LoginDialog
                isOpen={isLoginOpen}
                onClose={() => setIsLoginOpen(false)}
                onSuccess={handleLoginSuccess}
            />
        </div>
    );
};

export default Header;