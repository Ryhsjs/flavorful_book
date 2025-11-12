async function deleteRecipe(id) {
    const result = confirm("Вы уверены, что хотите удалить этот рецепт?");

    if (result) {
        let requestParams = {
            method: 'DELETE'
        }

        await fetch(CONTEXT_PATH + '/recipe/edit/' + id, requestParams);

        window.location.href = (new URL(BASE_URL + "/profile")).toString();
    }
}

async function deleteReview(id) {
    const result = confirm("Вы уверены, что хотите удалить отзыв?");

    if (result) {
        let requestParams = {
            method: 'DELETE'
        }

        await fetch(CONTEXT_PATH + '/recipe/review?id=' + id, requestParams);

        location.reload();
    }
}

function logout() {
    const result = confirm("Вы уверены, что хотите выйти?");

    if (result)
        window.location.href = (BASE_URL + "/logout");
}

function cancel() {
    history.back();
}

function goTo(path, params = {}) {
    const url = new URL(BASE_URL + path);

    Object.keys(params).forEach(key => {
        url.searchParams.set(key, params[key]);
    });

    window.location.href = url.toString();
}

async function addToFavorites() {
    let requestParams = {
        method: 'POST'
    }

    await fetch(location.href, requestParams);
    location.reload();

}

async function removeFromFavorites() {
    let requestParams = {
        method: 'DELETE'
    }

    await fetch(location.href, requestParams);
    location.reload();

}

function showPopup() {
    const blockout = document.getElementById("blockout");
    const body = document.getElementById("body");

    body.classList.add("no-scroll");
    blockout.classList.remove("none");

    document.addEventListener('keydown', function (e) {
        if (e.key === 'Escape') {
            closePopup();
        }
    });
}

function closePopup() {
    const blockout = document.getElementById("blockout");
    const body = document.getElementById("body");

    body.classList.remove("no-scroll");
    blockout.classList.add("none");
}

function editReview() {
    const userReview = document.getElementById("review-user");
    const editReview = document.getElementById("review-edit");

    if (userReview.classList.contains("none")) {
        userReview.classList.remove("none");
        editReview.classList.add("none");
    } else {
        userReview.classList.add("none");
        editReview.classList.remove("none");
    }
}

function setRating(rating) {
    const ratingInput = document.getElementById("rating");

    ratingInput.value = rating;

    for (let i = 1; i <= rating; i++) {
        const star = document.getElementById(`star-${i}`);
        star.classList.remove("text-subtle");
    }

    for (let i = rating + 1; i <= 5; i++) {
        const star = document.getElementById(`star-${i}`);
        star.classList.add("text-subtle");
    }
}

async function uploadImage() {
    const file = document.getElementById("image").files[0];
    const type = document.getElementById("type").value;

    if (!file) {
        return;
    }

    const formData = new FormData();
    formData.append('image', file);


    const response = await fetch(CONTEXT_PATH + '/image/' + type, {
        method: 'POST',
        body: formData
    });

    const result = await response.text();

    if (result) {
        const imageUrl = document.getElementById("imageUrl");
        imageUrl.value = result;
    }

}

function changeReview() {
    const textInput = document.getElementById('comment');
    const reviewFooter = document.getElementById('review-footer');

    if (textInput.value.length > 0) {
        reviewFooter.classList.remove("none");
    } else {
        reviewFooter.classList.add("none");
    }
}