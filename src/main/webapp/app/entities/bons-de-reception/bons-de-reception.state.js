(function() {
    'use strict';

    angular
        .module('pfeApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('bons-de-reception', {
            parent: 'entity',
            url: '/bons-de-reception',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'pfeApp.bons_de_Reception.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/bons-de-reception/bons-de-receptions.html',
                    controller: 'Bons_de_ReceptionController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('bons_de_Reception');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('bons-de-reception-detail', {
            parent: 'bons-de-reception',
            url: '/bons-de-reception/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'pfeApp.bons_de_Reception.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/bons-de-reception/bons-de-reception-detail.html',
                    controller: 'Bons_de_ReceptionDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('bons_de_Reception');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Bons_de_Reception', function($stateParams, Bons_de_Reception) {
                    return Bons_de_Reception.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'bons-de-reception',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('bons-de-reception-detail.edit', {
            parent: 'bons-de-reception-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/bons-de-reception/bons-de-reception-dialog.html',
                    controller: 'Bons_de_ReceptionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Bons_de_Reception', function(Bons_de_Reception) {
                            return Bons_de_Reception.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('bons-de-reception.new', {
            parent: 'bons-de-reception',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/bons-de-reception/bons-de-reception-dialog.html',
                    controller: 'Bons_de_ReceptionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                dateReception: null,
                                objet: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('bons-de-reception', null, { reload: 'bons-de-reception' });
                }, function() {
                    $state.go('bons-de-reception');
                });
            }]
        })
        .state('bons-de-reception.edit', {
            parent: 'bons-de-reception',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/bons-de-reception/bons-de-reception-dialog.html',
                    controller: 'Bons_de_ReceptionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Bons_de_Reception', function(Bons_de_Reception) {
                            return Bons_de_Reception.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('bons-de-reception', null, { reload: 'bons-de-reception' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('bons-de-reception.delete', {
            parent: 'bons-de-reception',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/bons-de-reception/bons-de-reception-delete-dialog.html',
                    controller: 'Bons_de_ReceptionDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Bons_de_Reception', function(Bons_de_Reception) {
                            return Bons_de_Reception.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('bons-de-reception', null, { reload: 'bons-de-reception' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
