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

const AddIcon = () => "+";
const CloseIcon = () => "x";

const DrawerForm = ({fetchTakers}) => {
    const {isOpen, onOpen, onClose} = useDisclosure()
    return <>
        <Button
            leftIcon={<AddIcon/>}
            colorScheme={"teal"}
            onClick={onOpen}
        >
            Add taker
        </Button>
        <Drawer isOpen={isOpen} onClose={onClose} size={"xl"}>
            <DrawerOverlay/>
            <DrawerContent>
                <DrawerCloseButton/>
                <DrawerHeader>Add taker</DrawerHeader>

                <DrawerBody>
                    <CreateTakerForm
                        fetchTakers={fetchTakers}
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

export default DrawerForm