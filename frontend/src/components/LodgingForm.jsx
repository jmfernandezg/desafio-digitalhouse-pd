import React, { useState, useEffect } from 'react';
import { Building2, MapPin, DollarSign, Star, Home, Image, Calendar, Heart } from 'lucide-react';

const InputField = ({ icon: Icon, label, ...props }) => (
    <div className="flex flex-col gap-1">
        <label className="text-sm font-medium text-gray-700">
            {label}
        </label>
        <div className="relative">
            <div className="absolute left-3 top-1/2 -translate-y-1/2 text-gray-400">
                <Icon size={18} />
            </div>
            <input
                {...props}
                className="w-full pl-10 pr-4 py-2 bg-white border border-gray-300 rounded-lg
          focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent
          placeholder-gray-400 hover:border-gray-400 transition-colors duration-200"
            />
        </div>
    </div>
);

const TextArea = ({ label, ...props }) => (
    <div className="flex flex-col gap-1">
        <label className="text-sm font-medium text-gray-700">
            {label}
        </label>
        <textarea
            {...props}
            className="w-full px-4 py-2 bg-white border border-gray-300 rounded-lg
        focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent
        placeholder-gray-400 hover:border-gray-400 transition-colors duration-200
        min-h-[100px] resize-y"
        />
    </div>
);

function LodgingForm({ onSubmit, lodging }) {
    const [formData, setFormData] = useState({
        name: '',
        address: '',
        city: '',
        country: '',
        price: '',
        stars: '',
        averageCustomerRating: '',
        description: '',
        category: '',
        availableFrom: '',
        availableTo: '',
        isFavorite: false,
        images: ['']  // Array for multiple image URLs
    });

    useEffect(() => {
        if (lodging) {
            setFormData({
                ...lodging,
                images: lodging.images || ['']  // Ensure images array exists
            });
        }
    }, [lodging]);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData({ ...formData, [name]: value });
    };

    const handleImageChange = (index, value) => {
        const newImages = [...formData.images];
        newImages[index] = value;
        setFormData({ ...formData, images: newImages });
    };

    const addImageField = () => {
        setFormData({ ...formData, images: [...formData.images, ''] });
    };

    const removeImageField = (index) => {
        const newImages = formData.images.filter((_, i) => i !== index);
        setFormData({ ...formData, images: newImages });
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        // Filter out empty image URLs
        const cleanedFormData = {
            ...formData,
            images: formData.images.filter(url => url.trim() !== '')
        };
        onSubmit(cleanedFormData);
    };

    return (
        <form onSubmit={handleSubmit} className="space-y-6 bg-gray-50 p-6 rounded-lg">
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                {/* Basic Information */}
                <InputField
                    icon={Building2}
                    label="Nombre del hospedaje"
                    type="text"
                    name="name"
                    value={formData.name}
                    onChange={handleChange}
                    placeholder="Nombre"
                    required
                />

                <InputField
                    icon={MapPin}
                    label="Dirección"
                    type="text"
                    name="address"
                    value={formData.address}
                    onChange={handleChange}
                    placeholder="Dirección"
                    required
                />

                <InputField
                    icon={MapPin}
                    label="Ciudad"
                    type="text"
                    name="city"
                    value={formData.city}
                    onChange={handleChange}
                    placeholder="Ciudad"
                    required
                />

                <InputField
                    icon={MapPin}
                    label="País"
                    type="text"
                    name="country"
                    value={formData.country}
                    onChange={handleChange}
                    placeholder="País"
                    required
                />

                <InputField
                    icon={DollarSign}
                    label="Precio por noche"
                    type="number"
                    name="price"
                    value={formData.price}
                    onChange={handleChange}
                    placeholder="Precio"
                    min="0"
                    required
                />

                <InputField
                    icon={Star}
                    label="Estrellas"
                    type="number"
                    name="stars"
                    value={formData.stars}
                    onChange={handleChange}
                    placeholder="Estrellas (1-5)"
                    min="1"
                    max="5"
                    required
                />

                <InputField
                    icon={Star}
                    label="Calificación promedio"
                    type="number"
                    name="averageCustomerRating"
                    value={formData.averageCustomerRating}
                    onChange={handleChange}
                    placeholder="Calificación (1-5)"
                    min="1"
                    max="5"
                    step="0.1"
                    required
                />

                <InputField
                    icon={Home}
                    label="Categoría"
                    type="text"
                    name="category"
                    value={formData.category}
                    onChange={handleChange}
                    placeholder="Categoría"
                    required
                />
            </div>

            {/* Description */}
            <TextArea
                label="Descripción"
                name="description"
                value={formData.description}
                onChange={handleChange}
                placeholder="Descripción del hospedaje"
                required
            />

            {/* Image URLs */}
            <div className="space-y-4">
                <div className="flex items-center justify-between">
                    <label className="text-sm font-medium text-gray-700">Imágenes</label>
                    <button
                        type="button"
                        onClick={addImageField}
                        className="px-4 py-2 text-sm text-blue-600 hover:bg-blue-50 rounded-lg transition-colors duration-200"
                    >
                        + Agregar imagen
                    </button>
                </div>
                {formData.images.map((url, index) => (
                    <div key={index} className="flex gap-2">
                        <div className="relative flex-1">
                            <div className="absolute left-3 top-1/2 -translate-y-1/2 text-gray-400">
                                <Image size={18} />
                            </div>
                            <input
                                type="url"
                                value={url}
                                onChange={(e) => handleImageChange(index, e.target.value)}
                                placeholder="URL de la imagen"
                                className="w-full pl-10 pr-4 py-2 bg-white border border-gray-300 rounded-lg
                  focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                            />
                        </div>
                        {index > 0 && (
                            <button
                                type="button"
                                onClick={() => removeImageField(index)}
                                className="px-3 py-2 text-red-600 hover:bg-red-50 rounded-lg transition-colors duration-200"
                            >
                                Eliminar
                            </button>
                        )}
                    </div>
                ))}
            </div>

            {/* Availability Dates */}
            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                <InputField
                    icon={Calendar}
                    label="Disponible desde"
                    type="datetime-local"
                    name="availableFrom"
                    value={formData.availableFrom}
                    onChange={handleChange}
                    required
                />

                <InputField
                    icon={Calendar}
                    label="Disponible hasta"
                    type="datetime-local"
                    name="availableTo"
                    value={formData.availableTo}
                    onChange={handleChange}
                    required
                />
            </div>

            {/* Favorite Toggle */}
            <div className="flex items-center gap-2">
                <input
                    type="checkbox"
                    id="isFavorite"
                    name="isFavorite"
                    checked={formData.isFavorite}
                    onChange={(e) => setFormData({ ...formData, isFavorite: e.target.checked })}
                    className="w-4 h-4 text-blue-600 border-gray-300 rounded focus:ring-blue-500"
                />
                <label htmlFor="isFavorite" className="flex items-center gap-2 text-sm text-gray-700">
                    <Heart size={16} className={formData.isFavorite ? 'text-red-500 fill-current' : 'text-gray-400'} />
                    Marcar como favorito
                </label>
            </div>

            {/* Submit Button */}
            <div className="flex justify-end pt-4">
                <button
                    type="submit"
                    className="px-6 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700
            focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2
            transition-colors duration-200"
                >
                    {lodging ? 'Actualizar' : 'Crear'} Hospedaje
                </button>
            </div>
        </form>
    );
}

export default LodgingForm;