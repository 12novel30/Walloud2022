import { css } from "@emotion/react";
import { useSetRecoilState } from "recoil";
import PersonBox from "../../component/box/personBox";
import { FontSize, ScreenSize } from "../../layout/globalStyle/globalSize";
import { personListState } from "../../recoils/travel";

const CreateBotton = css`
  opacity: 0.5;
  border: none;
  outline: none;
  width: 25px;
  height: 25px;
  border-radius: 25px;
  font-size: 2px;
  font-weight: bold;
  margin-top: 4%;
  margin-bottom: 4%;
  margin-left: 48%;
  transition-duration: 0.3s;
  &:hover {
    opacity: 0.7;
  }
  &:focus {
    border: none;
    outline: none;
  }
`;

function PersonSection(
  personList: any[],
  travelId: number,
  isManager: boolean
) {
  const setPersonList = useSetRecoilState(personListState);
  const personLength = personList.length;
  console.log(personList);

  const PersonSectionStyle = css`
    min-height: auto;
    & > :nth-of-type(1) {
      font-size: ${FontSize.fs18};
      padding: 10px 10px;
      border-bottom: 2px solid white;
    }
    & > :nth-of-type(2) {
      display: grid;
      grid-template-columns: 1fr 1fr 1fr;
    }
  `;

  function PersonType(type: string) {
    const PersonBoxs = personList
      .filter(
        (person) =>
          (person.role && type === "Manager") ||
          (!person.role && person.difference >= 0 && type === "Recieve") ||
          (person.difference < 0 && type === "Send")
      )
      .map((selectPerson, idx) => {
        return PersonBox(
          selectPerson,
          selectPerson.personId,
          selectPerson.userId,
          type,
          travelId,
          isManager,
          personList,
          setPersonList
        );
      });
    return PersonBoxs;
  }

  return (
    <div css={PersonSectionStyle}>
      <div>Person</div>
      {/* <button css = {CreateBotton} onClick={() => {setOpenEventModal(true)}}>+</button> */}
      <div>
        {PersonType("Send")}
        {PersonType("Manager")}
        {PersonType("Recieve")}
      </div>
    </div>
  );
}

export default PersonSection;
