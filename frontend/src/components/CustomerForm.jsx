import React, {useEffect, useState} from 'react';
import {Calendar, Lock, Mail, User} from 'lucide-react';

const InputField = ({icon: Icon, ...props}) => (<div className="relative">
        <div className="absolute left-3 top-1/2 -translate-y-1/2 text-gray-400">
            <Icon size={18}/>
        </div>
        <input
            {...props}
            className="w-full px-10 py-2 bg-white border border-gray-300 rounded-lg focus:outline-none
        focus:ring-2 focus:ring-blue-500 focus:border-transparent placeholder-gray-400
        hover:border-gray-400 transition-colors duration-200"
        />
    </div>);

function CustomerForm({onSubmit, customer}) {
    const [formData, setFormData] = useState({
        username: '', password: '', firstName: '', lastName: '', dob: '', email: '',
    });

    useEffect(() => {
        if (customer) {
            setFormData(customer);
        }
    }, [customer]);

    const handleChange = (e) => {
        const {name, value} = e.target;
        setFormData({...formData, [name]: value});
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        onSubmit(formData);
        setFormData({
            username: '', password: '', firstName: '', lastName: '', dob: '', email: '',
        });
    };

    return (<form onSubmit={handleSubmit} className="space-y-6 bg-gray-50 p-6 rounded-lg">
            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                {/* Username Field */}
                <InputField
                    icon={User}
                    type="text"
                    name="username"
                    value={formData.username}
                    onChange={handleChange}
                    placeholder="Usuario"
                    required
                />

                {/* Password Field */}
                <InputField
                    icon={Lock}
                    type="password"
                    name="password"
                    value={formData.password}
                    onChange={handleChange}
                    placeholder="Contraseña"
                    required
                />

                {/* First Name Field */}
                <InputField
                    icon={User}
                    type="text"
                    name="firstName"
                    value={formData.firstName}
                    onChange={handleChange}
                    placeholder="Nombre(s)"
                    required
                />

                {/* Last Name Field */}
                <InputField
                    icon={User}
                    type="text"
                    name="lastName"
                    value={formData.lastName}
                    onChange={handleChange}
                    placeholder="Apellido"
                    required
                />

                {/* Date of Birth Field */}
                <InputField
                    icon={Calendar}
                    type="date"
                    name="dob"
                    value={formData.dob}
                    onChange={handleChange}
                    required
                />

                {/* Email Field */}
                <InputField
                    icon={Mail}
                    type="email"
                    name="email"
                    value={formData.email}
                    onChange={handleChange}
                    placeholder="Email"
                    required
                />
            </div>

            <div className="flex justify-end pt-4">
                <button
                    type="submit"
                    className="px-6 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700
            focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2
            transition-colors duration-200"
                >
                    {customer ? 'Actualizar' : 'Crear'} Cliente
                </button>
            </div>
        </form>);
}

export default CustomerForm;