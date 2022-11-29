import { OrderInfo } from "../../../models";
//import { useAppSelector } from "../../../store/hook";
import { CgMoreVertical } from "react-icons/cg";
import { Link } from "react-router-dom";
const OrderItem = ({ order: orderInfo }: { order: OrderInfo }) => {
  // const {accessToken, user} = useAppSelector((state) => state.auth);

  return (
    <>
      <tr>
        <td>
          <span>{orderInfo.id}</span>
        </td>
        <td>{orderInfo.fullName || "Guest"}</td>
        <td>{orderInfo.orderDate}</td>
        <td>{orderInfo.quantity}</td>
        <td>${orderInfo.total}</td>
        <td>{orderInfo.status}</td>
        <td>
          <Link to={`/dashboard/orders/${orderInfo.id}`} title="More">
            <CgMoreVertical color="black" />
          </Link>
        </td>
      </tr>
    </>
  );
};

export default OrderItem;
