import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { LodgingService } from '../api/lodgingService';
import { Card, CardContent, CardMedia, Typography } from '@mui/material';

function LodgingDetail() {
    const { id } = useParams();
    const [lodging, setLodging] = useState(null);
    const [error, setError] = useState(null);
    const [isLoading, setIsLoading] = useState(true);

    useEffect(() => {
        const fetchLodging = async () => {
            try {
                setIsLoading(true);
                const data = await LodgingService.getLodgingById(id);
                setLodging(data);
            } catch (err) {
                setError('Error loading lodging details. Please try again.');
                console.error('Error loading lodging details:', err);
            } finally {
                setIsLoading(false);
            }
        };

        fetchLodging();
    }, [id]);

    if (error) {
        return <div className="error-message">{error}</div>;
    }

    if (isLoading) {
        return <div className="loading">Loading lodging details...</div>;
    }

    return (
        <div className="lodging-detail">
            {lodging && (
                <Card sx={{ maxWidth: 345 }}>
                    <CardMedia
                        component="img"
                        height="140"
                        image={lodging.displayPhoto}
                        alt={lodging.name}
                    />
                    <CardContent>
                        <Typography gutterBottom variant="h5" component="div">
                            {lodging.name}
                        </Typography>
                        <Typography variant="body2" color="text.secondary">
                            {lodging.description}
                        </Typography>
                        <Typography variant="body2" color="text.secondary">
                            Price: ${lodging.price.toFixed(2)}
                        </Typography>
                        <Typography variant="body2" color="text.secondary">
                            Stars: {lodging.stars}
                        </Typography>
                        <Typography variant="body2" color="text.secondary">
                            Address: {lodging.address}
                        </Typography>
                    </CardContent>
                </Card>
            )}
        </div>
    );
}

export default LodgingDetail;