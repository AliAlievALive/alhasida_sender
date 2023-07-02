import {
    Alert,
    AlertIcon,
    Box,
    Button,
    Flex,
    FormLabel,
    Heading,
    Image,
    Input,
    Select,
    Stack,
    Text,
} from '@chakra-ui/react';
import {Form, Formik, useField} from "formik";
import * as Yup from 'yup'
import {useAuth} from "../context/AuthContext.jsx";
import {errorNotification} from "../../services/notification.js";

const MyTextInput = ({label, ...props}) => {
    const [field, meta] = useField(props);
    return (
        <Box>
            <FormLabel htmlFor={props.id || props.name}>{label}</FormLabel>
            <Input className="text-input" {...field} {...props} />
            {meta.touched && meta.error ? (
                <Alert className="error" status={"error"} mt={2}>
                    <AlertIcon/>{meta.error}
                </Alert>
            ) : null}
        </Box>
    )
}

const LoginForm = () => {
    const { login } = useAuth();
    return <Formik
        validateOnMount={true}
        validationSchema={Yup.object({
            username: Yup.string()
                .email("Must be valid email")
                .required("Email is required"),
            password: Yup.string()
                .min(5, "Password cannot be less than 5 characters")
                .max(20, "Password cannot be more than 20 characters")
                .required("Password is required")
        })}
        initialValues={{username: '', password: ''}}
        onSubmit={(values, {setSubmitting}) => {
            setSubmitting(true);
            login(values).then(res => {
                // TODO: navigate to dashboard
                console.log(res);
            }).catch(err => {
                errorNotification(err.code, err.response.data.message)
            }).finally(() => {
                setSubmitting(false);
            });
        }}>

        {({isValid, isSubmitting}) => (
            <Form>
                <Stack spacing={15}>
                    <MyTextInput label={"Email"}
                                 name={"username"}
                                 type={"email"}
                                 placeholder={"test@mail.com"}
                    />
                    <MyTextInput label={"Password"}
                                 name={"password"}
                                 type={"password"}
                                 placeholder={"Type your password"}
                    />

                    <Button
                        type={"submit"}
                        isDisabled={!isValid || isSubmitting}>
                        Login
                    </Button>
                </Stack>
            </Form>
        )}
    </Formik>;
}

const Login = () => {

    return (
        <Stack minH={'100vh'} direction={{base: 'column', md: 'row'}}>
            <Flex p={8} flex={1} alignItems={'center'} justifyContent={'center'}>
                <Stack spacing={4} w={'full'} maxW={'md'} align={'center'} justify={'center'}>
                    <Box textAlign={'center'}>
                        <Image
                            borderRadius='full'
                            boxSize='150px'
                            src='LOGO.jpeg'
                            alt='FromPast'
                        />
                    </Box>
                    <Heading fontSize={'2xl'} mb={15}>Sign in to your account</Heading>
                    <LoginForm/>
                </Stack>
            </Flex>
            <Flex
                flex={1}
                p={10}
                flexDirection={"column"}
                alignItems={"center"}
                justifyContent={"center"}
                bgGradient={{sm: 'linear(to-r, blue.600, purple.600'}}>
                <Text fontSize={"6xl"} color={"white"} fontWeight={"bold"} mb={5}/>
                <Image
                    alt={'Login Image'}
                    objectFit={'cover'}
                    src='INTRO.jpeg'
                />
            </Flex>
        </Stack>
    );
}

export default Login;