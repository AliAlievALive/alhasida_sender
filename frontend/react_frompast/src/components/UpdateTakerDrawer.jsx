import {
    Button,
    Drawer,
    DrawerBody,
    DrawerCloseButton,
    DrawerContent,
    DrawerFooter,
    DrawerHeader,
    DrawerOverlay,
    Input,
    useDisclosure
} from "@chakra-ui/react";
import CreateTakerForm from "./CreateTakerForm.jsx";
import * as PropTypes from "prop-types";
import UpdateTakerForm from "./UpdateTakerForm.jsx";

const AddIcon = () => "+";
const CloseIcon = () => "x";

function UpdateTakerFrom(props) {
    return null;
}

UpdateTakerFrom.propTypes = {};
const UpdateTakerDrawer = ({fetchTakers, initialValues, takerId}) => {
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
            Update taker
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
                    />
                </DrawerBody>

                <DrawerFooter>
                    <Button leftIcon={<CloseIcon/>}
                            colorScheme={"teal"}
                            onClick={onClose}
                            type='submit'
                            form='my-form'
                    >
                        Close
                    </Button>
                </DrawerFooter>
            </DrawerContent>
        </Drawer>
    </>

}

export default UpdateTakerDrawer