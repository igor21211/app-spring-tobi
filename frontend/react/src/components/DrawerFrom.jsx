import {
    Button,
    Drawer,
    DrawerBody,
    DrawerCloseButton,
    DrawerContent, DrawerFooter,
    DrawerHeader,
    DrawerOverlay, Input, useDisclosure
} from "@chakra-ui/react"
import CreateCustomerForm from "./CreateCustomerForm.jsx";

const Addicon = () => "+";

const DrawerForm = () => {
    const { isOpen, onOpen, onClose } = useDisclosure()
    return <>
    <Button
        leftIcon={<Addicon/>}
        colorScheme={"teal"}
        onClick={onOpen}
    >
        Create Customer
    </Button>
     <Drawer isOpen={isOpen} onClose={onClose} size={"xl"}>
     <DrawerOverlay />
     <DrawerContent>
       <DrawerCloseButton />
       <DrawerHeader>Create your account</DrawerHeader>

       <DrawerBody>
         <CreateCustomerForm></CreateCustomerForm>
       </DrawerBody>

       <DrawerFooter>
         <Button type='submit' form='my-form'>
           Save
         </Button>
       </DrawerFooter>
     </DrawerContent>
   </Drawer>
   </>
    
}

export default DrawerForm;
