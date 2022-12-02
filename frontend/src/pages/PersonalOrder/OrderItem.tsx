import { useState } from "react";
import { PersonalOrder } from "../../models";
import { formatStr } from "../../utils";
import { SlSettings } from "react-icons/sl";
import { Link } from "react-router-dom";
import AppModal from "../../components/AppModal/AppModal";
import { Button } from "react-bootstrap";
interface Message {
  status: "success" | "fail";
  content: string;
}
const OrderItem = (props: { order: PersonalOrder }) => {
  const [message, setMessage] = useState<Message | null>(null);
  const [deleteModal, setDeleteModal] = useState<boolean>(false);
  const closeModal = (show: boolean) => {
    setDeleteModal(show);
    setMessage(null);
  };

  const handleDelete = async () => {
    if (props.order.status !== "PENDING") {
      return setMessage({
        status: "fail",
        content: "Can not delete this order!",
      });
    }
  };
  return (
    <>
      <tr>
        <td>{formatStr(props.order.id, 4)}</td>
        <td>{String(props.order.orderDate)}</td>
        <td>{props.order.address}</td>
        <td>{props.order.quantity}</td>
        <td>{props.order.total}</td>
        <td>{props.order.status}</td>
        <td className="iconSetting">
          <SlSettings className="icon" />
          <div className="action">
            <Link to={`/user/orderDetail/${props.order.id}`}>Detail</Link>
            <div className="divider"></div>
            <div className="deleteBt" onClick={() => setDeleteModal(true)}>
              Delete
            </div>
          </div>
          <div>
            <AppModal
              title={`Delete order - ${props.order.id}`}
              showModal={deleteModal}
              setShowModal={closeModal}
            >
              <Button>cancel</Button>
            </AppModal>
          </div>
        </td>
      </tr>
    </>
  );
};
export default OrderItem;
