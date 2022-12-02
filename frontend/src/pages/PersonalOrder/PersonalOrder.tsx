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

const OrderDetail = () => {
  const accessToken = useAppSelector((state) => state.auth.accessToken);
  const [orderList, setOrderList] = useState<PersonalOrder[]>([]);

  useLayoutEffect(() => {
    const fetchApi = async () => {
      try {
        const result = await getOrder(accessToken);
        setOrderList(result.content);
      } catch (error) {
        if (isAxiosError(error)) {
          console.log(error);
        }
      }
    };

    fetchApi();
  }, [orderList]);

  return (
    <Wrapper>
      <div className="orderContainer">
        <Table
          className="m-3"
          style={{ width: "700px" }}
          striped
          hover
          responsive
        >
          <thead id="orderHead">
            <tr>
              <th>Id</th>
              <th>Date</th>
              <th>Address</th>
              <th>quantity</th>
              <th>total</th>
              <th>status</th>
              <th>Setting</th>
            </tr>
          </thead>
          <tbody>
            {orderList.map((order) => (
              <OrderItem key={order.id} order={order} />
            ))}
          </tbody>
        </Table>
      </div>
    </Wrapper>
  );
};
export default OrderDetail;
