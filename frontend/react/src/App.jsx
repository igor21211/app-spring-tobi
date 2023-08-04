import {Spinner, Text, Wrap, WrapItem} from '@chakra-ui/react'
import SidebarWithHeader from "./components/shared/SideBar.jsx";
import React, {useEffect, useState} from "react";
import {getCustomers} from "./services/client.js";
import SocialProfileWithImage from "./components/Card.jsx";
import CardWithImage from "./components/Card.jsx";
import DrawerForm from "./components/DrawerFrom.jsx"


const App = () =>{

    const [customers, setCustomers] = useState([]);
    const[loading, setLoading] = useState(false);


    useEffect(()=>{
        setLoading(true);
        getCustomers().then(res=>{
            setCustomers(res.data)
        }).catch(err =>{
            console.log(err)
        }).finally(()=>{
            setLoading(false);
        })
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

    if(customers.length <=0){
        return (
            <SidebarWithHeader>
                <Text>No customers</Text>
            </SidebarWithHeader>
        )
    }
    return(
        <SidebarWithHeader>
            <DrawerForm></DrawerForm>
            <Wrap justify={"space-around"} spacing={"30px"}>
            {customers.map((customer, index) =>(
                <WrapItem key={index}>
                 <CardWithImage{...customer}/>
                </WrapItem>
            ))}
            </Wrap>
        </SidebarWithHeader>
    )
}

export default App;