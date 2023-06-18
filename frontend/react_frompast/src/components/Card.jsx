import {
    AlertDialog,
    AlertDialogBody,
    AlertDialogContent,
    AlertDialogFooter,
    AlertDialogHeader,
    AlertDialogOverlay,
    Avatar,
    Box,
    Button,
    Center,
    Flex,
    Heading,
    Image,
    Stack,
    Text,
    useColorModeValue,
    useDisclosure,
} from '@chakra-ui/react';
import {useRef} from "react";
import {deleteTaker} from "../services/client.js";
import {errorNotification, successNotification} from "../services/notification.js";
import UpdateTakerDrawer from "./UpdateTakerDrawer.jsx";

export default function CardWithImage({id, name, email, age, gender, imageNumber, fetchTakers}) {
    const genderIn = gender === "MALE" ? "men" : "women";
    const {isOpen, onOpen, onClose} = useDisclosure()
    const cancelRef = useRef()
    const handleDelete = () =>
        deleteTaker(id)
            .then(() => {
                successNotification("Taker deleted", `${name} was successfully deleted`);
                fetchTakers()
                onClose();
            }).catch(err => {
            console.log(err);
            errorNotification(err.code, err.response.data.message)
        });

    return (
        <Center py={6}>
            <Box
                maxW={'250px'}
                minW={'250px'}
                w={'full'}
                m={2}
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
                    objectFit={'cover'}
                />
                <Flex justify={'center'} mt={-12}>
                    <Avatar
                        size={'xl'}
                        src={`https://randomuser.me/api/portraits/${genderIn}/${imageNumber}.jpg`}
                        alt={'Taker'}
                        css={{
                            border: '2px solid white',
                        }}
                    />
                </Flex>

                <Box p={6}>
                    <Stack spacing={0} align={'center'} mb={5}>
                        <Heading fontSize={'2xl'} fontWeight={500} fontFamily={'body'}>
                            {name}
                        </Heading>
                        <Text color={'gray.500'}>{email}</Text>
                        <Text color={'gray.500'}>Age {age} | {gender}</Text>
                    </Stack>
                </Box>

                <Stack direction={'row'} justify={'center'} spacing={6} p={4}>
                    <Stack>
                        <UpdateTakerDrawer
                            initialValues={{name, email, age}}
                            takerId={id}
                            fetchTakers={fetchTakers}
                        />
                    </Stack>
                    <Stack>
                        <Button
                            bg={'red.400'}
                            color={'white'}
                            rounded={'full'}
                            _hover={{
                                transform: 'translateY(-2px)',
                                boxShadow: 'lg'
                            }}
                            _focus={{
                                bg: 'green.500'
                            }}
                            onClick={onOpen}
                        >
                            Delete
                        </Button>
                    </Stack>

                    <AlertDialog
                        isOpen={isOpen}
                        leastDestructiveRef={cancelRef}
                        onClose={onClose}
                    >
                        <AlertDialogOverlay>
                            <AlertDialogContent>
                                <AlertDialogHeader fontSize='lg' fontWeight='bold'>
                                    Delete Taker
                                </AlertDialogHeader>

                                <AlertDialogBody>
                                    Are you sure want to delete {name}? You can't undo this action afterwards.
                                </AlertDialogBody>

                                <AlertDialogFooter>
                                    <Button ref={cancelRef} onClick={onClose}>
                                        Cancel
                                    </Button>
                                    <Button colorScheme='red' onClick={handleDelete} ml={3}>
                                        Delete {name}
                                    </Button>
                                </AlertDialogFooter>
                            </AlertDialogContent>
                        </AlertDialogOverlay>
                    </AlertDialog>
                </Stack>
            </Box>
        </Center>
    );
}