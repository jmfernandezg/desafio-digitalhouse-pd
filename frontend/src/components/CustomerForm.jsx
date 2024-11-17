import React, { useState, useEffect } from 'react';

function CustomerForm({ onSubmit, customer }) {
    const [formData, setFormData] = useState({
        username: '',
        password: '',
        firstName: '',
        lastName: '',
        dob: '',
        email: '',
    });

    useEffect(() => {
        if (customer) {
            setFormData(customer);
        }
    }, [customer]);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData({ ...formData, [name]: value });
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        onSubmit(formData);
        setFormData({
            username: '',
            password: '',
            firstName: '',
            lastName: '',
            dob: '',
            email: '',
        });
    };

    return (
        <form className="admin-form" onSubmit={handleSubmit}>
            <input type="text" name="username" value={formData.username} onChange={handleChange} placeholder="Username" required />
            <input type="password" name="password" value={formData.password} onChange={handleChange} placeholder="Password" required />
            <input type="text" name="firstName" value={formData.firstName} onChange={handleChange} placeholder="First Name" required />
            <input type="text" name="lastName" value={formData.lastName} onChange={handleChange} placeholder="Last Name" required />
            <input type="date" name="dob" value={formData.dob} onChange={handleChange} required />
            <input type="email" name="email" value={formData.email} onChange={handleChange} placeholder="Email" required />
            <button type="submit">Submit</button>
        </form>
    );
}

export default CustomerForm;