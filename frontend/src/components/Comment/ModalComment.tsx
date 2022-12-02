import { Rating, Button, Box } from "@mui/material";
import { ChangeEvent, useState } from "react";
import { Form, Modal } from "react-bootstrap";
import { styled } from "@mui/material/styles";
import { IconContainerProps } from "@mui/material/Rating";
import SentimentVeryDissatisfiedIcon from "@mui/icons-material/SentimentVeryDissatisfied";
import SentimentDissatisfiedIcon from "@mui/icons-material/SentimentDissatisfied";
import SentimentSatisfiedIcon from "@mui/icons-material/SentimentSatisfied";
import SentimentSatisfiedAltIcon from "@mui/icons-material/SentimentSatisfiedAltOutlined";
import SentimentVerySatisfiedIcon from "@mui/icons-material/SentimentVerySatisfied";
import { addComment, updateComment } from "../../apis/comment";
import { useAppSelector } from "../../store/hook";

const StyledRating = styled(Rating)(({ theme }) => ({
  "& .MuiRating-iconEmpty .MuiSvgIcon-root": {
    color: theme.palette.action.disabled,
  },
}));

const customIcons: {
  [index: string]: {
    icon: React.ReactElement;
    label: string;
  };
} = {
  1: {
    icon: <SentimentVeryDissatisfiedIcon color="error" fontSize="large" />,
    label: "Very Dissatisfied",
  },
  2: {
    icon: <SentimentDissatisfiedIcon color="warning" fontSize="large" />,
    label: "Dissatisfied",
  },
  3: {
    icon: <SentimentSatisfiedIcon color="primary" fontSize="large" />,
    label: "Neutral",
  },
  4: {
    icon: <SentimentSatisfiedAltIcon color="success" fontSize="large" />,
    label: "Satisfied",
  },
  5: {
    icon: <SentimentVerySatisfiedIcon color="success" fontSize="large" />,
    label: "Very Satisfied",
  },
};

const IconContainer = (props: IconContainerProps) => {
  const { value, ...other } = props;
  return <span {...other}>{customIcons[value].icon}</span>;
};

interface ModalProps {
  onHide: () => void;
  show: boolean;
  bookid: number;
  sent: boolean;
  setsent: (c: boolean) => void;
  comment?: {
    rate: number;
    content: string;
  };
}

const ModalComment = (props: ModalProps) => {
  const accessToken = useAppSelector((state) => state.auth.accessToken);
  const [content, setContent] = useState<string>(
    props.comment !== undefined ? props.comment.content : ""
  );
  const [value, setValue] = useState<number | null>(3);
  const [hover, setHover] = useState(-1);
  const [rate, setRate] = useState<number>(
    props.comment !== undefined ? props.comment.rate : 0
  );

  const handleChangeContent = (e: ChangeEvent<HTMLInputElement>) => {
    let tmp = e.target.value;
    if (tmp.length <= 1000) setContent(tmp);
    else setContent(tmp.slice(0, 1000));
  };

  const sendReview = () => {
    if (props.comment !== undefined) {
      if (props.comment.content === content && props.comment.rate === rate)
        props.onHide();
      updateComment(
        { bookId: props.bookid, star: rate, content: content },
        {
          headers: {
            Authorization: `Bearer ${accessToken}`,
          },
        }
      );
    } else
      addComment(
        { bookId: props.bookid, star: rate, content: content },
        {
          headers: {
            Authorization: `Bearer ${accessToken}`,
          },
        }
      );
    props.setsent(true);
    props.onHide();
  };

  return (
    <Modal
      size="lg"
      {...props}
      aria-labelledby="contained-modal-title-vcenter"
      centered
    >
      <Modal.Header closeButton={true}>
        <Modal.Title id="contained-modal-title-vcenter">
          Write a Review
        </Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <h6>Tap a Star to Rate</h6>
        <Rating
          name="simple-controlled"
          value={rate}
          onChange={(event, newValue) => {
            setRate(newValue as number);
          }}
        />
        <Form.Control
          style={{ marginTop: "10px" }}
          as="textarea"
          rows={5}
          placeholder="Write a comment..."
          value={content}
          onChange={handleChangeContent}
        />
        <p style={{ color: "#666", fontSize: "14px" }}>
          Character count: {content === undefined ? 0 : content.length}/1000
        </p>
        <hr />
        <p>How satisfied are you with our services?</p>
        <Box
          sx={{
            display: "flex",
            alignItems: "center",
          }}
        >
          <StyledRating
            name="highlight-selected-only"
            defaultValue={3}
            IconContainerComponent={IconContainer}
            getLabelText={(value: number) => customIcons[value].label}
            highlightSelectedOnly
            onChange={(event: any, newValue: any) => {
              setValue(newValue);
            }}
            onChangeActive={(event: any, newHover: number) => {
              setHover(newHover);
            }}
          />
          {value !== null && (
            <Box sx={{ ml: 2 }}>
              {customIcons[hover !== -1 ? hover : value].label}
            </Box>
          )}
        </Box>
      </Modal.Body>
      <Modal.Footer>
        <Button
          variant="contained"
          onClick={props.onHide}
          color="secondary"
          size="small"
        >
          Close
        </Button>
        &nbsp;&nbsp;
        <Button
          variant="contained"
          color="primary"
          size="small"
          onClick={sendReview}
        >
          Send
        </Button>
      </Modal.Footer>
    </Modal>
  );
};

export default ModalComment;
