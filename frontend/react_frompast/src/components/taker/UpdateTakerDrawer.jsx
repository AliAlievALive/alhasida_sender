import {
    Button,
    Drawer,
    DrawerBody,
    DrawerCloseButton,
    DrawerContent,
    DrawerFooter,
    DrawerHeader,
    DrawerOverlay,
    useDisclosure
} from "@chakra-ui/react";
import UpdateTakerForm from "./UpdateTakerForm.jsx";

const AddIcon = () => "+";
const CloseIcon = () => "x";

function UpdateTakerFrom(props) {
    return null;
}

UpdateTakerFrom.propTypes = {};
const UpdateTakerDrawer = ({fetchTakers, initialValues, takerId, senderId}) => {
    const {isOpen, onOpen, onClose} = useDisclosure()
    return <>
        <Button
            bg={'teal'}
            color={'white'}
            rounded={'full'}
            _hover={{
                transform: 'translateY(-2px)',
                boxShadow: 'lg'
            }}
            onClick={onOpen}
        >
            Update
        </Button>
        <Drawer isOpen={isOpen} onClose={onClose} size={"xl"}>
            <DrawerOverlay/>
            <DrawerContent>
                <DrawerCloseButton/>
                <DrawerHeader>Update {initialValues.name}</DrawerHeader>

                <DrawerBody>
                    <UpdateTakerForm
                        fetchTakers={fetchTakers}
                        initialValues={initialValues}
                        takerId={takerId}
                        senderId={senderId}
                    />
                </DrawerBody>

                <DrawerFooter>
                    <Button leftIcon={<CloseIcon/>}
                            colorScheme={"teal"}
                            onClick={onClose}
                            type='submit'
                            form='my-form'
                    >
                        Cancel
                    </Button>
                </DrawerFooter>
            </DrawerContent>
        </Drawer>
    </>

}

export default UpdateTakerDrawer