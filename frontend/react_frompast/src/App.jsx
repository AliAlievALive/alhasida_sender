import SidebarWithHeader from "./components/shared/SideBar.jsx";
import {Spinner, Text, Wrap, WrapItem} from "@chakra-ui/react";
import {useEffect, useState} from "react";
import {getSenders} from "./services/client.js";
import CardWithImage from "./components/shared/Card.jsx";

const App = () => {

    const [senders, setSenders] = useState([]);
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        setLoading(true);
        getSenders().then(res => {
            setSenders(res.data);
            setLoading(false);
        }).catch(err => console.log(err)
        ).finally(() =>
            setLoading(false)
        );
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

    if (senders.length <= 0) {
        return (
            <SidebarWithHeader>
                <Text>No senders available</Text>
            </SidebarWithHeader>
        )
    }

    return (
        <SidebarWithHeader>
            <Wrap justify={"center"} spacing={"30px"}>
                {senders.map((sender, index) => (
                    <WrapItem key={index}>
                        <CardWithImage {...sender}/>
                    </WrapItem>
                ))}
            </Wrap>
        </SidebarWithHeader>
    );
};

export default App
