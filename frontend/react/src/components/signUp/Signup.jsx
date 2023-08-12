import {useAuth} from "../context/AuthContext.jsx";
import {useNavigate} from "react-router-dom";
import React, {useEffect} from "react";
import {Flex, Heading, Image, Link, Stack, Text} from "@chakra-ui/react";
import CreateCustomerForm from "../customer/CreateCustomerForm.jsx";

const Signup = () =>{
    const{customer, setCustomerFromToken} = useAuth();
    const navigate = useNavigate();

    useEffect(()=> {
        if(customer){
            navigate("/dashboard")
        }
    })

    return (
        <Stack minH={'100vh'} direction={{ base: 'column', md: 'row' }}>
            <Flex p={8} flex={1} alignItems={'center'} justifyContent={'center'}>
                <Stack spacing={4} w={'full'} maxW={'md'} >
                    <Image borderRadius='full'
                           boxSize='150px'
                           src='https://images.crunchbase.com/image/upload/c_lpad,h_170,w_170,f_auto,b_white,q_auto:eco,dpr_1/ap7mhfgvjzfy1v6jyn7n'
                           alignSelf={"center"}
                           mb={5}
                    ></Image>
                    <Heading fontSize={'2xl'} mb={15} >Register for an account</Heading>
                    <CreateCustomerForm onSuccess={(token)=> {
                        localStorage.setItem("access_token", token);
                        setCustomerFromToken();
                        navigate("/dashboard");
                    }} />
                    <Link color={"blue"} href={"/"}>Have an account? Login now.</Link>
                </Stack>
            </Flex>
            <Flex flex={1}
                  p={10}
                  flexDirection={"column"}
                  alignItems={"center"}
                  justifyContent={"center"}
                  bgGradient={{sm: 'linear(to-r, blue.600, purple.600)'}}
            >
                <Text fontSize={"6xl"} color={"white"} fontWeight={"bold"} mb={5}>
                    <Link href={"https://github.com/igor21211/app-spring-tobi"}>
                        What this
                    </Link>
                </Text>
                <Image
                    alt={'Login Image'}
                    objectFit={'scale-down'}
                    src={
                        'https://user-images.githubusercontent.com/40702606/215539167-d7006790-b880-4929-83fb-c43fa74f429e.png'
                    }
                />
            </Flex>
        </Stack>
    );
}

export default  Signup;