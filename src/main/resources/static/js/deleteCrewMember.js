function deleteCrewMember(passNumber) {
  const answer = confirm("Do you want to delete crew member?")

  if (!answer) {
    return
  }

  $.ajax({
    url: '/crew/' + passNumber,
    type: 'DELETE',
    dataType: 'html',
    success: (data) => {
      window.location.href = '/crew'
    }
  })
}