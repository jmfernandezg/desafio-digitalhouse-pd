import React from 'react';
import * as Select from '@radix-ui/react-select';
import { CheckIcon, ChevronDownIcon, ChevronUpIcon } from '@radix-ui/react-icons';
import './SortDropdown.css';

function SortDropdown({ sortOption, setSortOption }) {
    return (
        <Select.Root value={sortOption} onValueChange={setSortOption}>
            <Select.Trigger className="SelectTrigger" aria-label="Ordenar por">
                <Select.Value placeholder="Ordenar por" />
                <Select.Icon className="SelectIcon">
                    <ChevronDownIcon />
                </Select.Icon>
            </Select.Trigger>
            <Select.Content className="SelectContent">
                <Select.ScrollUpButton className="SelectScrollButton">
                    <ChevronUpIcon />
                </Select.ScrollUpButton>
                <Select.Viewport className="SelectViewport">
                    <Select.Group>
                        <Select.Label className="SelectLabel">Ordenar por</Select.Label>
                        <SelectItem value="price">Precio</SelectItem>
                        <SelectItem value="stars">Estrellas</SelectItem>
                        <SelectItem value="averageCustomerRating">Calificación</SelectItem>
                        <SelectItem value="distanceFromDownTown">Distancia del Centro</SelectItem>
                    </Select.Group>
                </Select.Viewport>
                <Select.ScrollDownButton className="SelectScrollButton">
                    <ChevronDownIcon />
                </Select.ScrollDownButton>
            </Select.Content>
        </Select.Root>
    );
}

const SelectItem = React.forwardRef(({ children, ...props }, ref) => {
    return (
        <Select.Item className="SelectItem" {...props} ref={ref}>
            <Select.ItemText>{children}</Select.ItemText>
            <Select.ItemIndicator className="SelectItemIndicator">
                <CheckIcon />
            </Select.ItemIndicator>
        </Select.Item>
    );
});

export default SortDropdown;