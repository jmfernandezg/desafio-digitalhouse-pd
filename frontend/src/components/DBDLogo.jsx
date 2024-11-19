import React, { useState } from 'react';

const DBDLogo = ({
                     width = '64',
                     height = '64',
                     className = '',
                     animated = true
                 }) => {
    const [isHovered, setIsHovered] = useState(false);

    return (
        <svg
            viewBox="0 0 500 500"
            width={width}
            height={height}
            className={`${className} transition-transform duration-300 ${isHovered ? 'scale-105' : ''}`}
            xmlns="http://www.w3.org/2000/svg"
            onMouseEnter={() => setIsHovered(true)}
            onMouseLeave={() => setIsHovered(false)}
        >
            <defs>
                <linearGradient id="skyGradient" x1="0" y1="0" x2="0" y2="1">
                    <stop offset="0%" stopColor="#8B5CF6">
                        {animated && (
                            <animate
                                attributeName="stop-color"
                                values="#8B5CF6; #9333EA; #8B5CF6"
                                dur="4s"
                                repeatCount="indefinite"
                            />
                        )}
                    </stop>
                    <stop offset="50%" stopColor="#EC4899">
                        {animated && (
                            <animate
                                attributeName="stop-color"
                                values="#EC4899; #DB2777; #EC4899"
                                dur="4s"
                                repeatCount="indefinite"
                            />
                        )}
                    </stop>
                    <stop offset="100%" stopColor="#F97316">
                        {animated && (
                            <animate
                                attributeName="stop-color"
                                values="#F97316; #EA580C; #F97316"
                                dur="4s"
                                repeatCount="indefinite"
                            />
                        )}
                    </stop>
                </linearGradient>

                {/* Animation for umbrella floating */}
                {animated && (
                    <animate
                        xlinkHref="#umbrella-group"
                        attributeName="transform"
                        attributeType="XML"
                        type="translate"
                        values="0,0; 0,-5; 0,0"
                        dur="3s"
                        repeatCount="indefinite"
                    />
                )}
            </defs>

            {/* Background Group */}
            <g transform="translate(100, 50)">
                <rect
                    x="0"
                    y="0"
                    width="300"
                    height="400"
                    rx="20"
                    fill="url(#skyGradient)"
                    className="transition-all duration-300"
                />

                {/* Umbrella Group with Animation */}
                <g id="umbrella-group">
                    <path
                        d="M150 50 L150 200
               M100 100 Q150 70 200 100"
                        stroke="#FFB800"
                        strokeWidth="8"
                        fill="none"
                        className={`transition-all duration-300 ${isHovered ? 'stroke-yellow-400' : ''}`}
                    >
                        {animated && (
                            <animate
                                attributeName="d"
                                values="M150 50 L150 200 M100 100 Q150 70 200 100;
                        M150 50 L150 200 M100 100 Q150 65 200 100;
                        M150 50 L150 200 M100 100 Q150 70 200 100"
                                dur="3s"
                                repeatCount="indefinite"
                            />
                        )}
                    </path>
                    <path
                        d="M100 100 Q150 130 200 100"
                        fill="#FFB800"
                        className={`transition-all duration-300 ${isHovered ? 'fill-yellow-400' : ''}`}
                    >
                        {animated && (
                            <animate
                                attributeName="d"
                                values="M100 100 Q150 130 200 100;
                        M100 100 Q150 125 200 100;
                        M100 100 Q150 130 200 100"
                                dur="3s"
                                repeatCount="indefinite"
                            />
                        )}
                    </path>
                </g>

                {/* Central B with hover effect */}
                <path
                    d="M120 100
             L120 300
             Q120 350 170 350
             Q220 350 220 300
             Q220 250 170 250
             Q220 250 220 200
             Q220 150 170 150
             Q120 150 120 100"
                    fill="white"
                    className={`transition-all duration-300 ${isHovered ? 'fill-gray-100' : ''}`}
                />

                {/* Animated Clouds/Landscape */}
                <path
                    d="M0 300
             Q50 280 100 300
             Q150 320 200 300
             Q250 280 300 300
             L300 400 L0 400 Z"
                    fill="#7C3AED"
                    opacity="0.7"
                    className="transition-all duration-300"
                >
                    {animated && (
                        <animate
                            attributeName="d"
                            values="M0 300 Q50 280 100 300 Q150 320 200 300 Q250 280 300 300 L300 400 L0 400 Z;
                      M0 300 Q50 285 100 300 Q150 315 200 300 Q250 285 300 300 L300 400 L0 400 Z;
                      M0 300 Q50 280 100 300 Q150 320 200 300 Q250 280 300 300 L300 400 L0 400 Z"
                            dur="5s"
                            repeatCount="indefinite"
                        />
                    )}
                </path>
            </g>

            {/* Side D letters with hover effect */}
            <g transform="translate(0, 50)">
                <path
                    d="M20 100
             L20 300
             Q20 350 70 350
             Q120 350 120 300
             L120 100
             Q120 50 70 50
             Q20 50 20 100"
                    fill="#1E1B4B"
                    className={`transition-all duration-300 ${isHovered ? 'fill-indigo-900' : ''}`}
                />
            </g>

            <g transform="translate(380, 50)">
                <path
                    d="M20 100
             L20 300
             Q20 350 70 350
             Q120 350 120 300
             L120 100
             Q120 50 70 50
             Q20 50 20 100"
                    fill="#1E1B4B"
                    className={`transition-all duration-300 ${isHovered ? 'fill-indigo-900' : ''}`}
                />
            </g>

            {/* Bottom D letters with hover effect */}
            <g transform="translate(0, 250)">
                <path
                    d="M20 100
             L20 300
             Q20 350 70 350
             Q120 350 120 300
             L120 100
             Q120 50 70 50
             Q20 50 20 100"
                    fill="#1E1B4B"
                    className={`transition-all duration-300 ${isHovered ? 'fill-indigo-900' : ''}`}
                />
            </g>

            <g transform="translate(380, 250)">
                <path
                    d="M20 100
             L20 300
             Q20 350 70 350
             Q120 350 120 300
             L120 100
             Q120 50 70 50
             Q20 50 20 100"
                    fill="#1E1B4B"
                    className={`transition-all duration-300 ${isHovered ? 'fill-indigo-900' : ''}`}
                />
            </g>
        </svg>
    );
};

export default DBDLogo;