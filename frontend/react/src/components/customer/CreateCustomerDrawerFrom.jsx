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
const Closeicon = () => "x";

const DrawerForm = ({fetchCustomers}) => {
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
       <DrawerHeader>Create new customer</DrawerHeader>

       <DrawerBody>
         <CreateCustomerForm
             fetchCustomers={fetchCustomers}
         ></CreateCustomerForm>
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

export default DrawerForm;

