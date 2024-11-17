import React, { useState } from 'react';
import ReactPaginate from 'react-paginate';

function LodgingList({ lodgings, onEdit, onDelete }) {
    const [currentPage, setCurrentPage] = useState(0);
    const lodgingsPerPage = 5;

    const handlePageClick = (data) => {
        setCurrentPage(data.selected);
    };

    const startIndex = currentPage * lodgingsPerPage;
    const currentLodgings = lodgings.slice(startIndex, startIndex + lodgingsPerPage);

    return (
        <div className="admin-list">
            <table>
                <thead>
                <tr>
                    <th>Nombre</th>
                    <th>Ciudad</th>
                    <th>Precio</th>
                    <th>Acciones</th>
                </tr>
                </thead>
                <tbody>
                {currentLodgings.map((lodging) => (
                    <tr key={lodging.id}>
                        <td>{lodging.name}</td>
                        <td>{lodging.city}</td>
                        <td>{lodging.price}</td>
                        <td>
                            <button onClick={() => onEdit(lodging)}>Editar</button>
                            <button onClick={() => onDelete(lodging.id)}>Eliminar</button>
                        </td>
                    </tr>
                ))}
                </tbody>
            </table>
            <ReactPaginate
                previousLabel={'Anterior'}
                nextLabel={'Siguiente'}
                breakLabel={'...'}
                pageCount={Math.ceil(lodgings.length / lodgingsPerPage)}
                marginPagesDisplayed={2}
                pageRangeDisplayed={5}
                onPageChange={handlePageClick}
                containerClassName={'pagination'}
                activeClassName={'active'}
            />
        </div>
    );
}

export default LodgingList;