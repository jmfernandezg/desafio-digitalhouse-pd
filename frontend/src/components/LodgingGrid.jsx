import React from 'react';
import {Link} from 'react-router-dom';
import {Card, CardContent, CardMedia, Typography} from '@mui/material';
import {Heart, MapPin} from 'lucide-react';
import './LodgingGrid.css';

function LodgingGrid({lodgings}) {
    const renderStars = (rating) => {
        const stars = [];
        for (let i = 0; i < 5; i++) {
            stars.push(
                <span key={i} className={i < rating ? 'star filled' : 'star empty'}>&#9733;</span>
            );
        }
        return stars;
    };

    return (
        <div className="grid-container">
            {lodgings.map(lodging => (
                <Card key={lodging.id} sx={{maxWidth: 345}} className="recommendation-card">
                    <div className="photo-container">
                        <CardMedia
                            component="img"
                            height="140"
                            image={lodging.displayPhoto}
                            alt={lodging.name}
                        />
                        <Heart
                            className={`heart-icon ${lodging.isFavorite ? 'favorite' : ''}`}
                            size={24}
                        />
                    </div>
                    <CardContent>
                        <Typography variant="h6" component="div">
                            <Link to={`/lodging/${lodging.id}`} style={{textDecoration: 'none', color: 'inherit'}}>
                                {lodging.name}
                            </Link>
                        </Typography>
                        <Typography variant="body2" color="text.secondary">
                            <span>&#36;{lodging.price.toFixed(2)}</span>
                        </Typography>
                        <div className="lodging-category-row">
                            <Typography variant="body2" color="text.secondary">
                                {lodging.category}
                            </Typography>
                            <div className="stars">{renderStars(lodging.stars)}</div>
                        </div>
                        <Typography variant="body2" color="text.secondary">
                            <MapPin size={16}/> {lodging.distanceFromDownTown.toFixed(2)} kms del centro
                            <a href={`https://maps.google.com/?q=${lodging.address}`} target="_blank"
                               rel="noopener noreferrer">
                                MOSTRAR MAPA
                            </a>
                        </Typography>
                    </CardContent>
                </Card>
            ))}
        </div>
    );
}

export default LodgingGrid;