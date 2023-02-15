import axios from "axios";
import UploadTravelImageAPI from "../../api/uploadTravelImageAPI";

const UploadTravelImageButton = (travelId: number, userId: number) => {
  const onChangeInput = (e: any) => {
    console.log("travel id: ", travelId, " user id: ", userId);
    var file: File = e.target.files[0];
    var reader: FileReader = new FileReader();

    reader.readAsDataURL(file);

    reader.onload = function () {
      const imageSrc = URL.createObjectURL(file);
      document.getElementById(
        `${travelId}-image`
      ).style.backgroundImage = `url(${imageSrc})`;
      document.getElementById(`${travelId}-image`).innerText = "";
      var formData = new FormData();
      formData.append("file", file);
      UploadTravelImageAPI(userId, travelId, formData);
    };
    console.log(document.getElementById(`${travelId}-image`));
  };

  return (
    <div style={{ display: "none" }}>
      <input
        id={`${travelId}-upload`}
        type="file"
        accept=".jpeg, .jpg, .png"
        onChange={(e) => onChangeInput(e)}
      />
    </div>
  );
};

export default UploadTravelImageButton;
