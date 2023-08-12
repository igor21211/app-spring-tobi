import {
    Button,
    Drawer,
    DrawerBody,
    DrawerCloseButton,
    DrawerContent, DrawerFooter,
    DrawerHeader,
    DrawerOverlay, Input, useDisclosure
} from "@chakra-ui/react"
import UpdateCustomerForm from "./UpdateCustomerForm.jsx";
import React from "react";

const Closeicon = () => "x";

const UpdateDrawerForm = ({fetchCustomers, initialValues, customerId}) => {
    const { isOpen, onOpen, onClose } = useDisclosure()
    return <>
        <Button colorScheme={"teal"} onClick={onOpen}>
            Update Customer
        </Button>
     <Drawer isOpen={isOpen} onClose={onClose} size={"xl"}>
     <DrawerOverlay />
     <DrawerContent>
       <DrawerCloseButton />
       <DrawerHeader>Update {initialValues.name}</DrawerHeader>

       <DrawerBody>
         <UpdateCustomerForm
             fetchCustomers={fetchCustomers}
             initialValues={initialValues}
             customerId={customerId}
         ></UpdateCustomerForm>
       </DrawerBody>

       <DrawerFooter>
           <Button
               leftIcon={<Closeicon/>}
               colorScheme={"teal"}
               onClick={onClose}
           >
               Close
           </Button>
       </DrawerFooter>
     </DrawerContent>
   </Drawer>
   </>
    
}

export default UpdateDrawerForm;

