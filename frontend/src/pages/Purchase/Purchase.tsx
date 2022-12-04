import { useLayoutEffect, useState } from "react";
import { useAppSelector } from "../../store/hook";
import styled from "styled-components";
import { getOrder } from "../../apis/order";
import { isAxiosError } from "../../apis/axiosInstance";
import "./index.css";
import { PersonalOrder } from "../../models";
import { Table } from "react-bootstrap";
import OrderItem from "./OrderItem";
const Wrapper = styled.div`
  background-color: #ffffff;
  position: relative;
  overflow: auto;
  min-height: 100vh;
  height: fit-content;
  display: flex;
  flex-direction: column;
`;

const Purchase = () => {
  const accessToken = useAppSelector((state) => state.auth.accessToken);
  const [orderList, setOrderList] = useState<PersonalOrder[]>([]);
  useLayoutEffect(() => {
    const fetchApi = async () => {
      try {
        const result = await getOrder(accessToken);
        console.log(result);
        setOrderList(result.content);
      } catch (error) {
        if (isAxiosError(error)) {
          console.log(error);
        }
      }
    };

    fetchApi();
  }, [accessToken]);

  return (
    <Wrapper>
      <div className="orderContainer">
        {orderList.map((order) => (
          <OrderItem key={order.id} order={order} />
        ))}
      </div>
    </Wrapper>
  );
};
export default Purchase;
