import axios from "axios";
import UpdateEventImageAPI from "../../api/updateEventImageAPI";

const EventImageBox = (eventId: number) => {
  axios.get(`/api/${eventId}/getEventImage`).then((response) => {
    console.log("image");
    const div = document.getElementById(`${eventId}-event-image`);
    // div.style.backgroundImage = `url(${response.data})`;
    // div.style.backgroundSize = "cover";
    const image = document.createElement("img");
    image.id = `${eventId}-img`;
    image.style.width = "100%";
    if (response.data === "") {
      image.src =
        "https://walloud-bucket-ver2.s3.ap-northeast-2.amazonaws.com/test/ac28ab47-ad36-49ba-84ab-0398f3324ee9gang.jpg";
    } else {
      image.src = response.data;
    }

    div.appendChild(image);
  });

  const onChangeInput = (e: any) => {
    var file: File = e.target.files[0];
    var reader: FileReader = new FileReader();

    reader.readAsDataURL(file);

    reader.onload = function () {
      const imageSrc = URL.createObjectURL(file);

      const div = document.getElementById(`${eventId}-event-image`);
      div.removeChild(document.getElementById(`${eventId}-img`));
      const newImage = document.createElement("img");
      newImage.src = imageSrc;
      newImage.id = `${eventId}-img`;
      newImage.style.width = "100%";
      newImage.style.height = "auto";
      div.appendChild(newImage);

      var formData = new FormData();
      formData.append("file", file);
      UpdateEventImageAPI(eventId, formData);
    };
  };

  return (
    <div
      id={`${eventId}-event-image`}
      style={{ backgroundColor: "black", width: "33.33%" }}
      onClick={(e) => document.getElementById(`${eventId}-upload`).click()}
    >
      <input
        id={`${eventId}-upload`}
        type="file"
        accept=".jpeg, .jpg, .png"
        onChange={(e) => onChangeInput(e)}
        style={{ display: "none" }}
      />
    </div>
  );
};

export default EventImageBox;
