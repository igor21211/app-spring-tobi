import {Spinner, Text, Wrap, WrapItem} from '@chakra-ui/react'
import SidebarWithHeader from "./components/shared/SideBar.jsx";
import React, {useEffect, useState} from "react";
import {getCustomers} from "./services/client.js";
import SocialProfileWithImage from "./components/Card.jsx";
import CardWithImage from "./components/Card.jsx";
import DrawerForm from "./components/CreateCustomerDrawerFrom.jsx"
import {errorNotification} from "./services/Notification.js";


const App = () =>{

    const [customers, setCustomers] = useState([]);
    const[loading, setLoading] = useState(false);
    const [err, setError] = useState("");

    const fetchCustomers = () =>{
        setLoading(true);
        getCustomers().then(res=>{
            setCustomers(res.data)
        }).catch(err =>{
            setError(err.response.data.message)
            errorNotification(
                err.code,
                err.response.data.message
            )
        }).finally(()=>{
            setLoading(false);
        })
    }

    useEffect(()=>{
        fetchCustomers()
    }, [])

    if(loading){
        return (
            <SidebarWithHeader>
                <Spinner
                    thickness='4px'
                    speed='0.65s'
                    emptyColor='gray.200'
                    color='blue.500'
                    size='xl'
                />
            </SidebarWithHeader>
        )
    }
    if(err){
        return (
            <SidebarWithHeader>
                <DrawerForm
                    fetchCustomers={fetchCustomers}
                ></DrawerForm>
                <Text mt={5}>Ops there was an error</Text>
            </SidebarWithHeader>
        )
    }

    if(customers.length <=0){
        return (
            <SidebarWithHeader>
                <DrawerForm
                    fetchCustomers={fetchCustomers}
                ></DrawerForm>
                <Text mt={5}>No customers</Text>
            </SidebarWithHeader>
        )
    }
    return(
        <SidebarWithHeader>
            <DrawerForm
                fetchCustomers={fetchCustomers}
            ></DrawerForm>
            <Wrap justify={"center"} spacing={"30px"}>
            {customers.map((customer, index) =>(
                <WrapItem key={index}>
                 <CardWithImage
                     {...customer}
                    imageNumber={index}
                     fetchCustomers={fetchCustomers}
                 />
                </WrapItem>
            ))}
            </Wrap>
        </SidebarWithHeader>
    )
}

export default App;