import axios from "axios";
import UploadUserImageAPI from "../../api/uploadUserImageAPI";

const UploadUserImageButton = (userId: number) => {
  const onChangeInput = (e: any) => {
    var file: File = e.target.files[0];
    var reader: FileReader = new FileReader();

    reader.readAsDataURL(file);

    reader.onload = function () {
      const imageSrc = URL.createObjectURL(file);
      document.getElementById(
        `${userId}-image`
      ).style.backgroundImage = `url(${imageSrc})`;
      document.getElementById(`${userId}-image`).innerText = "";
      var formData = new FormData();
      formData.append("file", file);
      UploadUserImageAPI(userId, formData);
    };
    console.log(document.getElementById(`${userId}-image`));
  };

  return (
    <div style={{ display: "none" }}>
      <input
        id={`${userId}-upload`}
        type="file"
        accept=".jpeg, .jpg, .png"
        onChange={(e) => onChangeInput(e)}
      />
    </div>
  );
};

export default UploadUserImageButton;
