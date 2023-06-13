import SidebarWithHeader from "./components/shared/SideBar.jsx";
import {Spinner, Text, Wrap, WrapItem} from "@chakra-ui/react";
import {useEffect, useState} from "react";
import {getTakers} from "./services/client.js";
import CardWithImage from "./components/shared/Card.jsx";

const App = () => {

    const [takers, setTakers] = useState([]);
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        setLoading(true);
        getTakers().then(res => {
            setTakers(res.data);
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

    if (takers.length <= 0) {
        return (
            <SidebarWithHeader>
                <Text>No takers available</Text>
            </SidebarWithHeader>
        )
    }

    return (
        <SidebarWithHeader>
            <Wrap justify={"center"} spacing={"30px"}>
                {takers.map((taker, index) => (
                    <WrapItem key={index}>
                        <CardWithImage {...taker}/>
                    </WrapItem>
                ))}
            </Wrap>
        </SidebarWithHeader>
    );
};

export default App
