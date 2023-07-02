import SidebarWithHeader from "./components/shared/SideBar.jsx";
import {Spinner, Text, Wrap, WrapItem} from "@chakra-ui/react";
import {useEffect, useState} from "react";
import {getTakersForSender} from "./services/client.js";
import CardWithImage from "./components/Card.jsx";
import CreateTakerDrawer from "./components/taker/CreateTakerDrawer.jsx";
import {errorNotification} from "./services/notification.js";

const App = () => {
    const senderId = localStorage.getItem("sender_id");
    const [takers, setTakers] = useState([]);
    const [loading, setLoading] = useState(false);
    const [err, setError] = useState("");

    const fetchTakers = () => {
        setLoading(true);
        getTakersForSender(senderId).then(res => {
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
                <CreateTakerDrawer
                    fetchTakers={fetchTakers}
                />
                <Text mt={5}>Oops there was an error</Text>
            </SidebarWithHeader>
        );
    }

    return (
        <SidebarWithHeader>
            <CreateTakerDrawer
                fetchTakers={fetchTakers}
                senderId={senderId}
            />
            <Wrap justify={"center"} spacing={"30px"}>
                {takers.map((taker, index) => (
                    <WrapItem key={index}>
                        <CardWithImage
                            {...taker}
                            imageNumber={index}
                            fetchTakers={fetchTakers}
                            senderId={senderId}
                        />
                    </WrapItem>
                ))}
            </Wrap>
        </SidebarWithHeader>
    );
};

export default App
