
const Bank = () => {
    const bank_List = [
        'KEB하나',
        'SC제일',
        '국민',
        '신한',
        '외환',
        '우리',
        '한국시티',
        '경남',
        '광주',
        '대구',
        '부산',
        '전북',
        '제주',
        '기업',
        '농협'
    ].sort()

    return (
        <div>
            <label htmlFor="Bank">Bank</label>
            <select id="Bank">
                {bank_List.map((bank,id) => 
                    <option value={bank} key={id}>{bank}</option>
                )}
            </select>
        </div>
    );
}

export default Bank;