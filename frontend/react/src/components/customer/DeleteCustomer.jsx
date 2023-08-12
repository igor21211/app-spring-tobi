import {
    AlertDialog,
    AlertDialogBody,
    AlertDialogContent,
    AlertDialogFooter,
    AlertDialogHeader,
    AlertDialogOverlay,
    Button, useDisclosure
} from "@chakra-ui/react";
import React from "react";
import {deleteCustomer} from "../../services/client.js";
import {errorNotification, successNotification} from "../../services/Notification.js";

export default function DeleteCustomer({id, name,  fetchCustomers}) {
    const { isOpen, onOpen, onClose } = useDisclosure()
    const cancelRef = React.useRef()

    return (
        <>
            <Button colorScheme='red' onClick={onOpen} mb={2}>
                Delete Customer
            </Button>

            <AlertDialog
                isOpen={isOpen}
                leastDestructiveRef={cancelRef}
                onClose={onClose}
            >
                <AlertDialogOverlay>
                    <AlertDialogContent>
                        <AlertDialogHeader fontSize='lg' fontWeight='bold'>
                            Delete Customer
                        </AlertDialogHeader>

                        <AlertDialogBody>
                            Are you sure? You can't undo this action afterwards.
                        </AlertDialogBody>

                        <AlertDialogFooter>
                            <Button ref={cancelRef} onClick={onClose}>
                                Cancel
                            </Button>
                            <Button colorScheme='red' onClick={()=>{
                                deleteCustomer(id)
                                    .then(res=>{
                                        fetchCustomers();
                                        successNotification(
                                            "Customer deleted",
                                            `${name} was deleted`
                                        )
                                    }).catch(err =>{
                                    setError(err.response.data.message)
                                    errorNotification(
                                        err.code,
                                        err.response.data.message
                                    )
                                }).finally(()=>{

                                })
                            }
                            } ml={3}>
                                Delete
                            </Button>
                        </AlertDialogFooter>
                    </AlertDialogContent>
                </AlertDialogOverlay>
            </AlertDialog>
        </>
    )
}

