import React from 'react';
import './Footer.css';
import { Facebook, Linkedin, Twitter, Instagram } from 'lucide-react';

function Footer() {
    return (
        <footer>
            <div className="footer-left">
                &copy; {new Date().getFullYear()} jmfernandezg. All rights reserved.
            </div>
            <div className="footer-right">
                <Facebook size={24} />
                <Linkedin size={24} />
                <Twitter size={24} />
                <Instagram size={24} />
            </div>
        </footer>
    );
}

export default Footer;