import SidebarWithHeader from "./components/shared/SideBar.jsx";
import {Spinner, Text, Wrap, WrapItem} from "@chakra-ui/react";
import {useEffect, useState} from "react";
import {getTakers} from "./services/client.js";
import CardWithImage from "./components/shared/Card.jsx";
import DrawerForm from "./components/DrawerForm.jsx";
import {errorNotification} from "./services/notification.js";

const App = () => {

    const [takers, setTakers] = useState([]);
    const [loading, setLoading] = useState(false);
    const [err, setError] = useState("");

    const fetchTakers = () => {
        setLoading(true);
        getTakers().then(res => {
            setTakers(res.data);
            setLoading(false);
        }).catch(err => {
                setError(err.response.data.message)
                errorNotification(err.code, err.response.data.message)
            }
        ).finally(() =>
            setLoading(false)
        );
    }

    useEffect(() => {
        fetchTakers()
    }, []);

    if (loading) {
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

    if (err) {
        return (
            <SidebarWithHeader>
                <DrawerForm
                    fetchTakers={fetchTakers}
                />
                <Text mt={5}>Oops there was an error</Text>
            </SidebarWithHeader>
        );
    }

    return (
        <SidebarWithHeader>
            <DrawerForm
                fetchTakers={fetchTakers}
            />
            <Wrap justify={"center"} spacing={"30px"}>
                {takers.map((taker, index) => (
                    <WrapItem key={index}>
                        <CardWithImage
                            {...taker}
                            imageNumber={index}
                            fetchTakers={fetchTakers}
                        />
                    </WrapItem>
                ))}
            </Wrap>
        </SidebarWithHeader>
    );
};

export default App
