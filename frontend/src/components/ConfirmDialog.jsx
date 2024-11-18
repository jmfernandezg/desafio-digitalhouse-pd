import React from "react";
import * as AlertDialog from "@radix-ui/react-alert-dialog";
import "./ConfirmDialog.css";
import {Button} from "@mui/material";

const ConfirmDialog = ({open, onClose, onConfirm}) => (
    <AlertDialog.Root open={open} onOpenChange={onClose}>
        <AlertDialog.Trigger asChild>
            <button className="Button violet">Eliminar Registro</button>
        </AlertDialog.Trigger>
        <AlertDialog.Portal>
            <AlertDialog.Overlay className="AlertDialogOverlay"/>
            <AlertDialog.Content className="AlertDialogContent">
                <AlertDialog.Title className="AlertDialogTitle">
                    Confirme la eliminación
                </AlertDialog.Title>
                <AlertDialog.Description className="AlertDialogDescription">
                    Esta acción no se puede deshacer. ¿Está seguro de que desea eliminar este registro?
                </AlertDialog.Description>
                <div style={{display: "flex", gap: 25, justifyContent: "flex-end"}}>
                    <AlertDialog.Cancel asChild>
                        <Button className="Button mauve" onClick={onClose}>Cancelar</Button>
                    </AlertDialog.Cancel>
                    <AlertDialog.Action asChild>
                        <Button className="Button red" onClick={onConfirm}>Si, eliminar</Button>
                    </AlertDialog.Action>
                </div>
            </AlertDialog.Content>
        </AlertDialog.Portal>
    </AlertDialog.Root>
);

export default ConfirmDialog;