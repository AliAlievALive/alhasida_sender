import {Form, Formik, useField} from 'formik';
import * as Yup from 'yup';
import {Alert, AlertIcon, Box, Button, FormLabel, Input, Stack} from "@chakra-ui/react";
import {updateTakerForSender} from "../services/client.js";
import {errorNotification, successNotification} from "../services/notification.js";

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

const UpdateTakerForm = ({fetchTakers, initialValues, takerId, senderId}) => {
    return (
        <>
            <Formik
                initialValues={initialValues}
                validationSchema={Yup.object({
                    name: Yup.string()
                        .max(15, 'Must be 15 characters or less')
                        .required('Required'),
                    email: Yup.string()
                        .email('Invalid email address')
                        .required('Required'),
                    age: Yup.number()
                        .min(18, 'Must be at least 18 years age')
                        .max(100, 'Must be less than 100 years age')
                        .required('Required')
                })}
                onSubmit={(updatedTaker, {setSubmitting}) => {
                    setSubmitting(true);
                    updateTakerForSender(senderId, takerId, updatedTaker)
                        .then(r => {
                            console.log(r);
                            successNotification("Taker updated", `${updatedTaker.name} was successfully updated`);
                            fetchTakers();
                        }).catch(err => {
                        console.log(err);
                        errorNotification(err.code, err.response.data.message)
                    }).finally(() => {
                        setSubmitting(false);
                    })
                }}
            >
                {({dirty, isValid, isSubmitting}) => (
                    <Form>
                        <Stack spacing={"24px"}>
                            <MyTextInput
                                label="Name"
                                name="name"
                                type="text"
                                placeholder="Jane"
                            />

                            <MyTextInput
                                label="Email Address"
                                name="email"
                                type="email"
                                placeholder="jane@formik.com"
                            />

                            <MyTextInput
                                label="Age"
                                name="age"
                                type="number"
                                placeholder="18"
                            />

                            <Button isDisabled={!dirty || !isValid || isSubmitting} type="submit">Submit</Button>
                        </Stack>
                    </Form>
                )}
            </Formik>
        </>
    );
};

export default UpdateTakerForm