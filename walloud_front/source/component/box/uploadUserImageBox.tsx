import axios from "axios";
import UploadUserImageAPI from "../../api/uploadUserImageAPI";

const UploadUserImageBox = (userId: number) => {
  axios
    .get(`/api/${userId}/getUserImage`)
    .then((response) => {
      const div = document.getElementById(`${userId}-image`);
      div.style.backgroundImage = `url(${response.data})`;
      div.style.backgroundSize = "cover";
    })
    .catch((error) => {
      console.log(error);
      if (error.response.data.status === 500) {
        alert(error.response.data.message);
      } else {
        alert("예기치 못한 오류가 발생했습니다");
      }
    });

  const onChangeInput = (e: any) => {
    var file: File = e.target.files[0];
    var reader: FileReader = new FileReader();

    reader.readAsDataURL(file);

    reader.onload = function () {
      const imageSrc = URL.createObjectURL(file);
      document.getElementById(
        `${userId}-image`
      ).style.backgroundImage = `url(${imageSrc})`;
      var formData = new FormData();
      formData.append("file", file);
      UploadUserImageAPI(userId, formData);
    };
  };

  return (
    <div
      id={`${userId}-image`}
      style={{ backgroundColor: "black", height: "300%", width: "300%" }}
      onClick={(e) => document.getElementById(`${userId}-upload`).click()}
    >
      <input
        id={`${userId}-upload`}
        type="file"
        accept=".jpeg, .jpg, .png"
        onChange={(e) => onChangeInput(e)}
        style={{ display: "none" }}
      />
    </div>
  );
};

export default UploadUserImageBox;
