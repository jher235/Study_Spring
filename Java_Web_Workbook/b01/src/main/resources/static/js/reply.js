async function get1(bno){
    const result = await axios.get(`/replies/list/${bno}`)

    // console.log(result)
    return result.data
}

async function getList({bno, page, size, goLast}){
    const result = await axios.get(`/replies/list/${bno}`, {params: {page, size}})

    return result.data;
}

