import UploadImageAPI from "../../api/uploadImageAPI";

const UploadImageButton = (id: number, userId: number) => {
  const onChangeInput = (e: any) => {
    var file: File = e.target.files[0];
    var reader: FileReader = new FileReader();

    reader.readAsDataURL(file);

    reader.onload = function () {
      const imageSrc = URL.createObjectURL(file);
      document.getElementById(
        `${id}-image`
      ).style.backgroundImage = `url(${imageSrc})`;
      document.getElementById(`${id}-image`).innerText = "";
      const formData = new FormData();
      formData.append("files", file);
      UploadImageAPI(userId, id, formData);
    };
    console.log(document.getElementById(`${id}-image`));
  };

  return (
    <div style={{ display: "none" }}>
      <input
        id={`${id}-upload`}
        type="file"
        accept=".jpeg, .jpg, .png"
        onChange={(e) => onChangeInput(e)}
      />
    </div>
  );
};

export default UploadImageButton;
