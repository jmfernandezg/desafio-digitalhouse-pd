import React from 'react';

function LodgingList({ lodgings, onEdit, onDelete }) {
    return (
        <div className="admin-list">
            <table>
                <thead>
                <tr>
                    <th>Name</th>
                    <th>City</th>
                    <th>Price</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                {lodgings.map((lodging) => (
                    <tr key={lodging.id}>
                        <td>{lodging.name}</td>
                        <td>{lodging.city}</td>
                        <td>{lodging.price}</td>
                        <td>
                            <button onClick={() => onEdit(lodging)}>Edit</button>
                            <button onClick={() => onDelete(lodging.id)}>Delete</button>
                        </td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
}

export default LodgingList;