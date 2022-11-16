import Modal from 'react-modal';
import { atom, useRecoilState } from 'recoil';
import { css } from '@emotion/react';
import Color from '../../layout/globalStyle/globalColor';

const ModalBody = css`
    position: absolute;
    width: 300px;
    height: 500px;
    padding: 40px;
    text-align: center;
    background-color: rgb(255, 255, 255);
    border-radius: 10px;
`

const ModalCloseBtn = css`
    position: absolute;
    top: 15px;
    right: 15px;
    border: none;
    color: rgba(0, 0, 0, 0.7);
    background-color: transparent;
    font-size: 20px;
    &:hover{
        cursor: pointer;
    }
`

const LeftModal = {

}


export const isopenModal = atom<boolean>({
    key:'openModal',
    default: false,
});

export const ModalContainer = (props : any, checkleft:boolean) => {
    const [isOpen,setisOpen] = useRecoilState(isopenModal);

    return (
        <Modal style = {{overlay: {
            position: 'fixed',
            top: 0,
            left: 0,
            right: 0,
            bottom: 0,
            backgroundColor: 'rgba(255, 255, 255, 0.5)'
          },
          content: {
            position: 'fixed',
            top: '100px',
            left: '50%',
            minWidth: '45%',
            right: '50px',
            bottom: '40px',
            border: '1px solid #ccc',
            background: Color.blue02,
            overflow: 'hidden',
            WebkitOverflowScrolling: 'touch',
            borderRadius: '4px',
            outline: 'none',
            padding: '20px'
          }}} isOpen={isOpen} onRequestClose={() => {setisOpen(false)}}>
            <button css={ModalCloseBtn} onClick={() => {setisOpen(false)}}>
            ✖
            </button>
            {props.children}
        </Modal>
    )
}
