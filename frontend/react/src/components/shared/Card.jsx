'use client'

import {
    Heading,
    Avatar,
    Box,
    Center,
    Image,
    Flex,
    Text,
    Stack,
    Button,
    useColorModeValue, Tag,
} from '@chakra-ui/react'
import DeleteCustomer from "../customer/DeleteCustomer.jsx";
import UpdateDrawerForm from "../customer/UpdateCustomerDrawerFrom.jsx";

export default function CardWithImage({id, name, email, age, gender, fetchCustomers}) {
    const randomGender = gender === "MALE" ? "men" : "women";
    return (
        <Center py={8}>
            <Box
                maxW={'300px'}
                minW={"300px"}
                w={'full'}
                bg={useColorModeValue('white', 'gray.800')}
                boxShadow={'lg'}
                rounded={'md'}
                overflow={'hidden'}>
                <Image
                    h={'120px'}
                    w={'full'}
                    src={
                        'https://images.unsplash.com/photo-1612865547334-09cb8cb455da?ixid=MXwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=634&q=80'
                    }
                    objectFit="cover"
                    alt="#"
                />
                <Flex justify={'center'}  mt={-12}>
                    <Avatar
                        size={'xl'}
                        src={
                            `https://randomuser.me/api/portraits/med/${randomGender}/75.jpg`
                        }
                        css={{
                            border: '2px solid white',
                        }}
                    />
                </Flex>

                <Box p={8}>
                    <Stack spacing={0} align={'center'} mb={5}>
                        <Tag borderRadius={"full"}>{id}</Tag>
                        <Heading fontSize={'2xl'} fontWeight={500} fontFamily={'body'}>
                            {name}
                        </Heading>
                        <Text color={'gray.500'}>{email}</Text>
                        <Text mb={5} color={'gray.500'}>Age {age} | {gender}</Text>
                        <DeleteCustomer  id={id} name={name} fetchCustomers={fetchCustomers}></DeleteCustomer>
                        <UpdateDrawerForm
                            initialValues={{name, email, age}}
                            fetchCustomers={fetchCustomers}
                            customerId={id}
                        />
                    </Stack>
                </Box>
            </Box>
        </Center>
    )
}