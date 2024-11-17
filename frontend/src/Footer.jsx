import React from 'react';
import {Link} from 'react-router-dom';
import './Footer.css';
import {Facebook, Instagram, Linkedin, Twitter, Settings} from 'lucide-react';

function Footer() {
    return (
        <footer>
            <div className="footer-left">
                &copy; {new Date().getFullYear()} jmfernandezg. All rights reserved.
            </div>
            <div className="footer-right">
                <Link to="/admin">
                    <Settings size={24}/>
                </Link>
                <Facebook size={24}/>
                <Linkedin size={24}/>
                <Twitter size={24}/>
                <Instagram size={24}/>
            </div>
        </footer>
    );
}

export default Footer;